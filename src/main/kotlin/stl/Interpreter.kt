package stl

import java.lang.Exception

fun eval(expr: Expr): Expr =
  when(expr) {
    is Add -> {
      val lhs = eval(expr.lhs)
      val rhs = eval(expr.rhs)
      if (lhs is Lit && rhs is Lit) {
        Lit(lhs.value + rhs.value)
      }
      else {
        throw Exception("add is for int only")
      }
    }
    is App -> {
      val f = eval(expr.f)
      val a = eval(expr.a)
      if (f is Fun) {
        eval(f.body.subst(f.arg, a))
      }
      else {
        throw Exception("not a function")
      }
    }
    is Tst -> {
      val cond = when (expr.cond) {
        is Eqn -> {
          val lhs = eval(expr.cond.lhs)
          val rhs = eval(expr.cond.rhs)
          if (lhs is Lit && rhs is Lit) {
            lhs.value == rhs.value
          }
          else {
            throw Exception("eqn is for int only")
          }
        }
        is Les -> {
          val lhs = eval(expr.cond.lhs)
          val rhs = eval(expr.cond.rhs)
          if (lhs is Lit && rhs is Lit) {
            lhs.value < rhs.value
          }
          else {
            throw Exception("les is for int only")
          }
        }
        else -> throw Exception("not a boolean value")
      }

      if (cond) eval(expr.csq) else eval(expr.alt)
    }
    is Var -> throw Exception("variable ${expr.name} is unbound")
    else -> expr
  }
