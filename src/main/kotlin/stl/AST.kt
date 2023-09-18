package stl

sealed class Expr {
}
interface BoolIR

data class Eqn(val lhs: Expr, val rhs: Expr): Expr(), BoolIR {}
data class Les(val lhs: Expr, val rhs: Expr): Expr(), BoolIR {}

data class Lit(val value: Int): Expr() {}
data class Add(val lhs: Expr, val rhs: Expr): Expr() {}
data class Fun(val arg: String, val body: Expr): Expr() {}
data class App(val f: Expr, val a: Expr): Expr() {}
data class Tst(val cond: BoolIR, val csq: Expr, val alt: Expr): Expr() {}
