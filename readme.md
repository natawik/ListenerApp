### Приложение должно слушать директорию на появление новых файлов:

+ При появлении нового файла необходимо вывести в лог его имя, его расширение и дату создания
+ После необходимо запустить в новом потоке его обработку, выбор обработчика осуществляется в зависимости от расширения
+ Обработчик должен вывести в лог данные о времени начала обработки и общее время обработки файла
+ Если файл по расширению не подходит под допустимый, необходимо запустить обработчик, который бы удалил данный файл
+ Допустимы два расширения: xml, json
+ В качестве имитации обработки необходимо вывести в лог количество строк в файле
+ Логгирование должно осуществляться в файл на диске
