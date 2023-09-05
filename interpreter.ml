exception RuntimeError of string;;
exception TypeError of Lang.st * Lang.st;;

let rec replace e nme v = match e with
  | Lang.Lit _ -> e
  | Lang.Add (lhs, rhs) -> Lang.Add ((replace lhs nme v), (replace rhs nme v))
  | Lang.Sub (lhs, rhs) -> Lang.Sub ((replace lhs nme v), (replace rhs nme v))
  | Lang.Les (lhs, rhs) -> Lang.Les ((replace lhs nme v), (replace rhs nme v))
  | Lang.Eqn (lhs, rhs) -> Lang.Eqn ((replace lhs nme v), (replace rhs nme v))
  | Lang.Var nme' ->
    if nme = nme' then v
    else e
  | Lang.Fun (nme', body) ->
    if nme = nme' then e
    else Lang.Fun (nme', (replace body nme v))
  | Lang.App (lhs, rhs) -> Lang.App ((replace lhs nme v), (replace rhs nme v))
  | Lang.TST (cond, csq, alt) ->
    Lang.TST ((replace cond nme v), (replace csq nme v), (replace alt nme v))
;;

let rec eval e = match e with
  | Lang.Lit n -> n
  | Lang.Add (lhs, rhs) -> (eval lhs) + (eval rhs)
  | Lang.Sub (lhs, rhs) -> (eval lhs) - (eval rhs)
  | Lang.Var nme -> raise (RuntimeError ("unbound variable: " ^ nme))
  | Lang.App (Lang.Fun (nme, body), p) -> (eval (replace body nme p))
  | Lang.TST (Lang.Les (lhs, rhs), csq, alt) ->
    let lhs' = eval lhs in let rhs' = eval rhs in
      if (lhs' < rhs') then (eval csq) else (eval alt)
  | Lang.TST (Lang.Eqn (lhs, rhs), csq, alt) ->
    let lhs' = eval lhs in let rhs' = eval rhs in
      if (lhs' = rhs') then (eval csq) else (eval alt)
  | _ -> raise (RuntimeError "not a number.")
;;

let typing e =
  let rec run e = match e with
    | Lang.Lit _ -> Lang.Int
    | _ -> raise (RuntimeError "not implemented yet.") (* TODO *)
    in let res = run e in (match res with
      | Lang.Int -> ()
      | _ -> raise (TypeError (Lang.Int, res)));;
