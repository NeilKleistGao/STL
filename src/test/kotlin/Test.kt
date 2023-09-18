import stl.*
import java.lang.Exception

fun print(expr: Expr): String {
  return when(expr) {
    is Lit -> expr.value.toString()
    is Add -> "${print(expr.lhs)} + ${print(expr.rhs)}"
    is Fun -> "(${expr.arg}) -> ${print(expr.body)}"
    is App -> "(${print(expr.f)} ${print(expr.a)})"
    is Eqn -> "${print(expr.lhs)} == ${print(expr.rhs)}"
    is Les -> "${print(expr.lhs)} < ${print(expr.rhs)}"
    is Tst -> "if (${print(expr.cond)}) ${print(expr.csq)} else ${print(expr.alt)}"
    else -> throw Exception("invalid case")
  }
}

fun test(prgm: Expr): Unit {
  // TODO: type

  val res = eval(Add(Lit(1), Lit(2)))
  println("res = ${print(res)}")
  println("=========")
}

fun main(args: Array<String>) {
  test(Add(Lit(1), Lit(2)))
}
