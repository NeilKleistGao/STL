import stl.*
import java.lang.Exception

fun print(expr: Expr): String =
  when(expr) {
    is Lit -> expr.value.toString()
    is Var -> expr.name
    is Add -> "${print(expr.lhs)} + ${print(expr.rhs)}"
    is Fun -> "(${expr.arg}) -> ${print(expr.body)}"
    is App -> "(${print(expr.f)} ${print(expr.a)})"
    is Eqn -> "${print(expr.lhs)} == ${print(expr.rhs)}"
    is Les -> "${print(expr.lhs)} < ${print(expr.rhs)}"
    is Tst -> "if (${print(expr.cond)}) ${print(expr.csq)} else ${print(expr.alt)}"
    else -> throw Exception("invalid case")
  }

fun test(prgm: Expr): Unit {
  val typer = Typer()
  val type = typer.type(prgm)

  if (typer.isSafe()) {
    val res = eval(prgm)
    println("res: ${typer.print(type)}")
    println("res = ${print(res)}")
  }

  println("=========")
}

fun main(args: Array<String>) {
  test(Add(Lit(1), Lit(2)))
  test(Fun("a", Var("a")))
  test(Fun("a", Add(Var("a"), Lit(1))))
  test(App(Fun("x", Add(Var("x"), Lit(2))), Lit(4)))
  test(Tst(Eqn(Lit(2), Add(Lit(1), Lit(1))), Lit(0), Lit(1)))
  test(Fun("f", Fun("x", App(Var("f"), Var("x")))))
}
