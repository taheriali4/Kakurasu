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

q1 :: Question
q1 = ([1,2],[1,2])

puzzle :: Question
puzzle = ([46,77,77,67,48,76,68,71,59,46,13,46],[73,56,58,68,39,66,49,56,66,41,44,52])

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
prune :: Question -> Matrix Choices -> Matrix Choices
prune q m = pruneBy rows( pruneBy cols m (snd q)) (fst q)
--prune x = pruneBy rows (pruneBy cols x)
        --where pruneBy f = f . map reduce . f
        --where pruneBy f x = f (map reduce (f x)
        where pruneBy f x q = f (map reduce (zip (f x) q))

{-
reduce :: Row Choices -> Row Choices 
reduce xss = [xs `minus` singles | xs <- xss]
        where singles = concat (filter single xss)
-}

--sets parts of Row Choices as single if theyre greater than the number
reduce ::  (Row Choices, Int) -> Row Choices
--reduce (xss,num) = [if i > (num - sumSingle xss) then [False] else [True,False] | (i,s) <- zip [1..] xss]
reduce (xss,num) = [help i num xss s | (i,s) <- zip [1..] xss]

help :: Int -> Int -> Row Choices -> Choices ->  Choices
help i num xss [s] = [s]
help i num xss s = if i > (num - sumSingle xss) then [False] else [True,False]

--if sumUnkowns xss == num then all non singles are True
--potentially nice prune

--sums up the current single squares in a row
sumSingle :: Row Choices -> Int
sumSingle r = fst (foldl (\(acc, num) x -> if (single x) && (head x) then (acc + num, num + 1) else (acc, num + 1)) (0,1) r)

--sums up single squares and unknowns.
sumUnknowns :: Row Choices -> Int
sumUnknowns = fst . foldl(\(acc,num) x -> if (not (single x)) || (head x) then (acc + num, num + 1) else (acc, num + 1)) (0,1) 

--if the unknowns sum to the number exactly then it has to be true
--setTrue :: Row Choices -> Int -> Row Choices
--setTrue r = if (sumSingle)

minus :: Choices -> Choices -> Choices
xs `minus` ys = if single xs then xs else xs \\ ys

--impos :: Choices -> Choices

single :: [a] -> Bool
single [_] = True
single _ = False

fix :: Eq a =>  (a -> a) -> a -> a
fix f x = if x == x' then x else fix f x'
    where x' = f x

solveFixPrune :: Question -> [Grid]
solveFixPrune q= filter (valid q) (collapse (fix (prune q) (choices (length (fst q)))))

--turns out that its not that good 
--we need to take out anything that 
--lets keep optimizing
solve :: Question -> [Grid]
solve q =  search q (prune q (choices (length  (fst  q))))

search :: Question -> Matrix Choices -> [Grid]
search q m
    | blocked m q = []
    | complete m = collapse m
    | otherwise = [g | m' <- guesses m
                     , g <- search q (prune q m')]


guesses :: Matrix Choices -> [Matrix Choices]
guesses m = 
    [rows1 ++ [row1 ++ [c] : row2] ++ rows2 | c<- cs]
    where
        (rows1, row : rows2) = break (any (not . single)) m --breaks up the rows into 3 parts
        (row1, cs : row2) = break (not . single) row -- breaks up the row into 3 parts
    
--this is a 'complete square'
complete :: Matrix Choices -> Bool
complete = all (all single)

-- 'impossible solution' might not be complete
blocked :: Matrix Choices -> Question -> Bool
blocked m q = void m || not (safe m q)

--checks if anylists are empty null == true on []
--not ever true so we should instead check if a row or col cant sum up anymore
void :: Matrix Choices -> Bool
void = any (any null)


--check that we are not going over the sums
safe :: Matrix Choices -> Question -> Bool
safe cm q = all consistent (zip (rows cm) (fst q)) &&
            all consistent (zip (cols cm) (snd q)) 

--if the ones we know about are 
consistent :: (Row Choices, Int) -> Bool
consistent (r, q) = (sumSingle r) <= q && (sumUnknowns r) >= q
--want to add up all the singles and then prove that theyre less than the sum


main :: IO ()
main = putStrLn (unlines (map show (map (map (\x -> if x then 1 else 0) ) p)))
    where
    p = head (solve puzzle)
--main = putStrLn (show (solve puzzle))
