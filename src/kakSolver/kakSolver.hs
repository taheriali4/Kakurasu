type Grid = Matrix Value
type Matrix a = Row a
type Row a = [a]
type Value = Bool

type Question a = ([a], [a])

values :: [Value]
values = [true, false]

puzzle :: Grid
puzzle = [[true,false],
          [false,true]]

valid :: Grid -> Question -> Bool
valid g (q1, q2) -> sumcheck (rows g) q1 && sumcheck (cols g) q2


--transpose :: Grid -> Grid
--transpose ([]:_) = []
--transpose x = (map head x) : transpose (map tail x)


rows :: Matrix a -> [Row a]
rows = id

cols :: Matrix a -> [Row a]
cols = transpose

--if uneven zzz
sumcheck :: [Row a] -> [a] -> boolean
sumcheck [] [] = true
sumcheck _ [] = error "uneven"
sumcheck [] _ = error "uneven"
sumcheck r:rs a:as = (foldl (\(num, count) x -> (if x then (num + count, count + 1) else (num, count + 1))) (0, 1) rs) == a && sumcheck rs as


--treat a list as a nondet set of values
type Choices = [Value]

choices :: Grid -> Matrix Choices
choices g = map (map choice) g
    where 
        choice v = if empty v then values else [v]




