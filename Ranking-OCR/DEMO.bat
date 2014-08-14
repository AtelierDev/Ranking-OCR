@echo off
REM Define path value
set f1=".\INPUT\Original.txt"
set f2=".\INPUT\Comparative1.txt"
set f3=".\INPUT\Comparative2.txt"
set f4=".\INPUT\Comparative3.txt"
set jar=".\dist\Ranking-OCR.jar"
set out=".\out.csv"
REM show the help
java -jar %jar% -help
REM Start comparisons
@echo on
java -jar %jar% -indoc1 %f1%  -indoc2 %f2% -outdoc %out% -ranker SimpleRanker 
java -jar %jar% -indoc1 %f1%  -indoc2 %f3% -outdoc %out% -ranker SimpleRanker 
java -jar %jar% -indoc1 %f1%  -indoc2 %f4% -outdoc %out% -ranker SimpleRanker 
pause