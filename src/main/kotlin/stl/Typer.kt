package stl

import kotlin.collections.HashMap

typealias TypeEq = Pair<Type, Type>

class Typer() {

  private val parents = HashMap<Type, Type>()
  private val ctx = HashMap<String, Type>()
  private val eqList = mutableListOf<TypeEq>()
  private val diagnostics = mutableListOf<String>()

  init {
    parents[IntType] = IntType
    parents[BoolType] = BoolType
    parents[NothingType] = NothingType
  }

  private fun find(t: Type): Type =
    if (parents[t] == t) t
    else {
      val p = parents[t]
      if (p is Type) {
        val tp = find(p)
        parents[t] = tp
        tp
      }
      else t
    }

  fun isSafe(): Boolean = diagnostics.isEmpty()

  fun type(prgm: Expr): Type {
    val res = visit(prgm)
    unify(eqList.iterator())
    diagnostics.forEach {
      println("Type Error: $it")
    }

    return find(res)
  }

  private fun merge(x: Type, y: Type) {
    val lhs = if (x !is TypeVar && y is TypeVar) y else x
    val rhs = if (lhs == x) y else x
    if (lhs != rhs) {
      when (lhs) {
        is TypeVar -> parents[lhs] = rhs
        is FunType ->
          if (rhs is FunType) {
            merge(lhs.arg, rhs.arg)
            merge(lhs.res, rhs.res)
          }
          else {
            raise("$rhs is not a function type.")
          }
        else -> raise("can not unify $lhs and $rhs")
      }
    }
  }

  private fun unify(eq: Iterator<TypeEq>) {
    if (eq.hasNext()) {
      val pair = eq.next()
      val lhs = find(pair.first)
      val rhs = find(pair.second)
      merge(lhs, rhs)
      unify(eq)
    }
  }

  private val sequence = sequence {
    var id = 0
    while (true) {
      yield(id)
      ++id
    }
  }

  private fun refresh(): TypeVar {
    val res = TypeVar(sequence.first())
    parents[res] = res
    return res
  }

  private fun raise(msg: String): NothingType {
    diagnostics.add(msg)
    return NothingType
  }

  private fun constrain(lhs: Type, rhs: Type) {
    eqList.add(Pair(lhs, rhs))
  }

  private fun visit(expr: Expr): Type =
    when (expr) {
      is Lit -> IntType
      is Var -> ctx.getOrElse(expr.name) { raise("unbound variable ${expr.name}") }
      is Add -> {
        val lhsTy = visit(expr.lhs)
        val rhsTy = visit(expr.rhs)
        constrain(lhsTy, IntType)
        constrain(rhsTy, IntType)
        IntType
      }
      is Fun -> {
        val tv = refresh()
        ctx[expr.arg] = tv
        val res = visit(expr.body)
        FunType(tv, res)
      }
      is App -> {
        val fTy = visit(expr.f)
        val aTy = visit(expr.a)
        val funTy = FunType(aTy, refresh())
        constrain(fTy, funTy)
        funTy.res
      }
      is Eqn -> {
        val lhsTy = visit(expr.lhs)
        val rhsTy = visit(expr.rhs)
        constrain(lhsTy, IntType)
        constrain(rhsTy, IntType)
        BoolType
      }
      is Les -> {
        val lhsTy = visit(expr.lhs)
        val rhsTy = visit(expr.rhs)
        constrain(lhsTy, IntType)
        constrain(rhsTy, IntType)
        BoolType
      }
      is Tst -> {
        val condTy = visit(expr.cond)
        val csqTy = visit(expr.csq)
        val altTy = visit(expr.alt)
        constrain(condTy, BoolType)
        constrain(csqTy, altTy)
        csqTy
      }
    }
}
