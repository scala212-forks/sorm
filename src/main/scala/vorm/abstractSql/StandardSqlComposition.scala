package vorm.abstractSql

import vorm._
import sql._
import extensions._

import sql.Compositing._

import AbstractSql._

object StandardSqlComposition {

  def sql
    ( statement : Statement )
    : Sql.Statement
    = statement match {
        case Union(l, r) => 
          Sql.Union( sql(l), sql(r) )
        case Intersection(l, r) => 
          sql(l) narrow sql(r)
        case s : Select =>

          val tables = SqlComposition.allTables(s)

          val aliases : Map[Table, String]
            = tables.view.zipWithIndex
                .map{ case(t, i) => t -> alias(i) }
                .toMap

          Sql.Select(
            what
              = s.expressions.toStream
                  .collect{ 
                    case Column(n, t) => 
                      Sql.Column(n, Some(aliases(t)))
                  },
            from
              = Sql.From(Sql.Table(tables.head.name), 
                         Some(aliases(tables.head))),
            join
              = tables.toStream.tail
                  .map{ t => 
                    Sql.Join(
                      Sql.Table(t.name),
                      Some(aliases(t)),
                      t.parent.get.bindings
                        .map{ case (l, r) =>
                          Sql.Column(l, Some(aliases(t))) ->
                          Sql.Column(r, Some(aliases(t.parent.get.table)))
                        }
                    )
                  },
            where
              = {
                def condition ( c : Condition ) : Sql.Condition[Sql.WhereObject]
                  = c match {
                      case And(l, r) => 
                        Sql.CompositeCondition( condition(l), condition(r), Sql.And )
                      case Or(l, r) => 
                        Sql.CompositeCondition( condition(l), condition(r), Sql.Or )
                      case Comparison(t, c, Equal, null) =>
                        Sql.IsNull( 
                          Sql.Column(c, Some(aliases(t)))
                        )
                      case Comparison(t, c, NotEqual, null) =>
                        Sql.IsNull(
                          Sql.Column(c, Some(aliases(t))), true
                        )
                      case Comparison(t, c, o, v) =>
                        Sql.Comparison(
                          Sql.Column(c, Some(aliases(t))),
                          Sql.Value(v),
                          SqlComposition.sql(o)
                        )
                    }
                s.condition map condition
              },
            groupBy
              = s.expressions.toStream
                  .collect{ case Column(n, t) => 
                    Sql.Column(n, Some(aliases(t))) 
                  },
            having
              = for { HavingCount(t, c) <- s.havingCount }
                yield Sql.Comparison(
                        Sql.Count(
                          Sql.AllColumns( Some(aliases(t)) ) :: Nil, 
                          true
                        ),
                        Sql.Value(c),
                        Sql.Equal
                      )
          )
      }

}