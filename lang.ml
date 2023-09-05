type expr =
  | Lit of int (* num *)
  | Add of expr * expr (* e + e *)
  | Sub of expr * expr (* e - e *)
  | Les of expr * expr (* e < e *)
  | Eqn of expr * expr (* e == e *)
  | Var of string (* n *)
  | Fun of string * expr (* fun x -> e *)
  | App of expr * expr (* e e *)
  | TST of expr * expr * expr (* if e then e else e *)
;; 

type st =
  | Int
  | Bool
  | Fun of (st * st)
  | TV of int
;;
