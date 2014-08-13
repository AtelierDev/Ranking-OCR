set f1="N:\Google Drive\Professionnel\Projet\Lecteur recette\Essais sur tesseract\Generateur\out\Text de base.txt"
set f2="N:\Google Drive\Professionnel\Projet\Lecteur recette\Essais sur tesseract\Generateur\out\out0.txt"
set jar=".\dist\Ranking-OCR.jar"
java -jar %jar% -origin %f1%  -compared %f2% -ranker SimpleRanker -decimal 5
echo %Errorlevel%
java -jar %jar% -help
pause