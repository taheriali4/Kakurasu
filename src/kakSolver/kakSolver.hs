import Data.List

type Grid = Matrix Value
type Matrix a = [Row a]
type Row a = [a]
type Value = Bool

type Question = ([Int], [Int]) --column then row?

values :: [Value]
values = [True, False]

--puzzle :: Grid
--puzzle = [[True,False],
         -- [False,True]]


valid :: Question -> Grid-> Bool
valid (q1, q2) g = (sumcheck (rows g) q1) && (sumcheck (cols g) q2)


--transpose :: Matrix a -> [Row a]
--transpose ([]:_) = []
--transpose x = (map head x) : transpose (map tail x)


rows :: Matrix a -> [Row a]
rows = id

cols :: Matrix a -> [Row a]
cols =  transpose 

--if uneven zzz
sumcheck ::Integral b =>  [Row Value] -> [b] -> Bool
sumcheck [] [] = True
sumcheck _ [] = error "uneven"
sumcheck [] _ = error "uneven"
sumcheck (r:rs) (a:as) = (fst(foldl (\(num, count) x -> if x then (num + count, count + 1) else (num, (count + 1))) (0, 1) r) == a ) && (sumcheck rs as)


--treat a list as a nondet set of values
type Choices = [Value]

--not big enough
--choices :: Grid -> Matrix Choices
--choices g = map (map choice) g
--    where 
--        choice v = if empty v then values else [v]

choices :: Int -> Matrix Choices
choices n = replicate n (replicate n [True, False])

-- makes each row a list of all possibilites
-- then makes each list of rows a list of each possibility
collapse :: Matrix [a] -> [Matrix a]
collapse = sequence . map sequence
--(map sequence lst) makes the combination, then the seq combo makes each possibilty ty cameron


solveBrute :: Question -> [Grid]
solveBrute question= filter (valid  question) (collapse (choices (length (fst question))))
--solveBrute question = filter valid . question collapse . choices . length . fst . question
--filter (verify) (possibilities)


--now we want to prune the search tree a little
prune :: Matrix Choices -> Matrix Choices
prune = pruneBy rows . pruneBy cols
        where pruneBy f = f . map reduce . f

{-
reduce :: Row Choices -> Row Choices 
reduce xss = [xs `minus` singles | xs <- xss]
        where singles = concat (filter single xss)
-}

--sets parts of Row Choices as single if theyre greater than the number
reduce :: Row Choices -> Int -> Row Choices
reduce xss = foldl (\(acc, num) x -> if (

--sums up the current single squares in a row
sumSingle :: Row Choices -> Int
sumSinge r = foldl (\(acc, num) x -> if (single x) && (head x) then (acc + num, num + 1) else (acc, num + 1)) (0,1) r

--sums up single squares and unknowns.
sumUnkowns :: Row Choices -> Int
sumUnknowns = foldl(\(acc,num) x -> if (not (single x)) || (head x) then (acc + num, num + 1) else (acc, num + 1) (0,1) 
--if the unknowns sum to the number exactly then it has to be true
setTrue :: Row Choices -> Int -> Row Choices
setTrue r = if (sumSingle)

minus :: Choices -> Choices -> Choices
xs `minus` ys = if single xs then xs else xs \\ ys

impos :: Choices -> Choices

single :: [a] -> Bool
single [_] = True
single _ = False

fix :: Eq a =>  (a -> a) -> a -> a
fix f x = if x == x' then x else fix f x'
    where x' = f x

solveFixPrune :: Question -> [Grid]
solveFixPrune q= filter (valid q) (collapse (fix prune (choices (length (fst q)))))

--turns out that its not that good - it doesnt do anything!
--we need to take out anything that 
--lets keep optimizing
{-
solve :: Question -> [Grid]
solve = search prune choices . length . fst 

search :: Matrix Choices -> [Grid]
search m
    | blocked m = []
    | complete m = collapse m
    | otherwise = [g | m' <- guesses m
                     , g <- search (prune m')]


guesses :: Matrix Choices -> [Matrix Choices]
guesses m = 
    [rows1 ++ [row1 ++ [c] : row2] ++ rows2 | c<- cs]
    where
        (rows1, row : row2) = break (any (not . single)) m --breaks up the rows into 3 parts
        (row1, cs : row2) = break (not . single) row -- breaks up the row into 3 parts
    
--this is a 'complete square'
complete :: Matrix Choices -> Bool
complete = all (all single)

-- 'impossible solution' might not be complete
blocked :: Matrix Choices -> Bool
blocked m = void m || not (safe m)

--checks if anylists are empty null == true on []
void :: Matrix Choices -> Bool
void = any (any null)

--check that we are not going over the sums
safe :: Matrix Choices -> Question -> Bool
safe cm q = all consistent (rows cm) (fst q) &&
            all consistent (cols cm) (fst q)

--if the ones we know about are 
consistent :: Row Choices -> [b] -> Bool

-}

