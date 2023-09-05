open Array;;

let init size = (init size (let id x = x in id));;

let rec find arr x =
  if arr.(x) = x then x
  else let p = (find arr (arr.(x))) in
    (arr.(x) <- p); p;;

let merge arr x y =
  let px = (find arr x) in
    let py = (find arr y) in
      if px != py then arr.(px) <- py;;
