# Bit_X
## Программирование Мобильных устройств
## тз

* зарегистрировать пользователя .
* добавить новых рабочих .
* добавить новых трейдеров .
* добавить новые проекты .
* у каждого проекта есть доходы и расходы .
* в приходах и расходах можно добавлять новые счета .
* Вы можете редактировать счета, рабочих, трейдеров и проекты.


***Оглавление*** 
**Введение** ............................................................................................................................ 7\
Читателям книги «Программирование для Android 5. Самоучитель)) ........................................ 8\
Как читать эrу книrу? ...................................................................................................................... 9\
Вкратце об Android .......................................................................................................................... 9 \
Выбор Android-ycтpoйcтвa ............................................................................................................ 12 \
Процессор ............................................................................................................................... 14\
Общие сведения ........................................................................................................... 14\
Подробнее об АRМ-процессорах ............................................................................... 15\
Выводы ......................................................................................................................... 20\
Память ..................................................................................................................................... 20\
Дисплей ................................................................................................................................... 21\
Видеоускоритель .................................................................................................................... 21\
Архитектура Android ..................................................................................................................... 23\
Google Play ...................................................................................................................................... 24\
**ЧАСТЬ 1. ПОДГОТОВКА К РАБОТЕ ...................................................................... 25 **\
**Глава 1. Установка необходимого программного обеспечения ........................... 27 **\
l. l. Что нужно для создания Аndгоid-приложения? ................................................................... 27\
1.2. Установка комплекта разработчика Jаvа-приложений- Java Development Kit (JDK) ....... 29\
1.3. Установка среды разработки Android Studio .......................................................................... 30\
1.3.l. Уровни API ................................................................................................................... 37\
1.3.2. Состав Android SDK ................................................................ , .................................... 37\
**Глава 2. Первый проект, первое приложение и эмулятор Android .................... 39 **\
2.1. Создание нового проекта ....................................................................................................... 39\
2.2. Структура приложения .......................................................................................................... .43\
2.3. Основное окно Android Studio ............................................................................................... 44\
2.4. Подготовка эмулятора Android .............................................................................................. 47\
2.5. Запуск приложения ..................................................... ~ ........................................................... 52\
2.5. l. В эмуляторе .................................................................................................................. 52\
2.5.2. На реальном устройстве .............................................................................................. 54\
2.6. Проблемы при запуске приложения ...................................................................................... 54\
2.6.l. Нюансы запуска приложения в эмуляторе ................................................................. 54\
2.6.2. Включение режима отладки по USB на реальном устройстве ................................. 55\
2.7. Управление вирrуальным устройством с помощью команды adb ..................................... 55\ 
2.8. Командная строка ................................................................................................................... 58\ 
2.8. l. Создание проекта из командной строки ..................................................................... 58\
2.8.2. Запуск проекта из командной строки ......................................................................... 58\
**4 Оглавление **\
2.9. Создание снимка экрана виртуального устройства ............................................................. 5S\ 
2.1 О. Подробно о системных требованиях .............................................. : .................................... 59\ 
**ЧАСТЬ П. БАЗОВОЕ ПРОГРАММИРОВАНИЕ ДЛЯ ANDROШ ...................... 61**\ 
**Глава 3. Осноры построения приложений ............................................................... 63 **\
3.1. Структура Android-пpoeктa .................................................................................................... 63\ 
3.2. Компоненты Аndrоid-приложения ........................................................................................ 70\
3.3. Процессы в ОС Android ......................................................................................................... 71\ 
3.4. Подробнее о файле AndroidManifest.xm/ ............................................................................... 72\ 
3.5. Разрешения Аndrоid-приложений ......................................................................................... 75\ 
**Глава 4. Интерфейс пользователя ............................................................................. 78 **\
4.1. Разметка интерфейса .............................................................................................................. 78\ 
4.1.1. Редактор разметки ............. .. ......................................................................................... 78\ 
4.\.2. Разные типы разметки ................................................ .. .... ................. ; ......................... 81\ 
Разметка FrameLayout ............................................................................. : ................... 82\ 
Разметка LinearLayout ................................................................................................. 82\
Разметка TaЬ/eLayout ................................................................................................... 83\ 
Разметка GridLayout .................................................................................................... 88\ 
Разметка RelativeLayout ............................................................................................... 91\ 
Разметка ConstraintLayout ........................................................................................... 92\ 
4.2. Основные виджеты графического интерфейса .................................................................... 97\ 
4.2.1. Текстовые поля ............................................................................................................. 98\ 
4.2.2. Кнопки ......................................................................................................................... 103\ 
Button - обычная кнопка ......................................................................................... 103\ 
RadioButton - зависимые переключатели .............................................................. 107\ 
CheckBox - независимые переключатели .............................................................. 108\ 
Togg/eButton - кнопка включено/выключено ........................................................ 109\ 
lmageButton - кнопка с изображением ................................................................... 111\ 
4.2.3. Индикатор ProgressBar .............................................................................................. 112\ 
4.2.4. Средства отображения графики ................................................................................ 116\ 
**Глава 5. Уведомления, диалоговые окна и меню ................................................. 118 **\
5.1. Уведомления ......................................................................................................................... 118\ 
5 .1.1. Простое всплывающее уведомление ........................................................................ 118 \
5.1.2. Уведомление в строке состояния .............................................................................. 120\ 
5.1.3. Каналы уведомлений в Android 9.0 .......................................................................... 124\ 
5.1.4. Определение дейGтвия уведомления ........................................................................ 124 \
5.1.5. Кнопки действия ........................................................................................................ 125 \
5.1.6. Удаление собственных уведомлений ....................................................................... 126 \
5 .1. 7. Звуковая, световая и вибросигнализация ................................................................. 126\ 
5.1.8. Вывод длинного текста .............................................................................................. 126 \
5.2. Диалоговые окна ................................................................................................................... 127\ 
5.2.1. Диалоговое окно AlertDialog ..................................................................................... 127 \
5.2.2. DatePickerDia/og: диалоговое окно выбора даты .................................................... 129 \
5.2.3. TimePickerDialog: диалоговое окно выбора времени .............................................. 134 \
5.3. Меню ...................................................................................................................................... 138 \
5.3.1. Определение меню в ХМL-файле ............................................................................. 138 \
5.3.2. Создание основного меню (меню параметров) ....................................................... 140 \
**Оглавление 5 **
5.3.3. Создание контекстного меIОО .................................................................................... 142 \
5.3.4. Создание всплывающего меIОО ................................................................................. 144 \
**Глава 6. Двумерная гра·фика .................................................................................... 147 **\
6.1. Класс DrawaЬ/e ..................................................................................................................... 147\ 
6.2. Класс TransitionDrawaЬ/e: переход между изображениями .............................................. 151\ 
6.3. Класс ShapeDrawaЬ/e ........................................................................................................... 155 \
**Глава 7. Му ль тимедиа ................................................................................................ 158 **\
7.1. Форматы мультимедиа, поддерживаемые ОС Android ..................................................... 158 \
7.2. Работа со звуком ................................................................................................................... 159 \
7.2.1. Используем MediaPlayer ........................................................................................... 159 \
7.2.2. Использование MediaRecorder: запись звука ........................................................... 160 
7 .2.3. Использование AudioRecord и А udio Track ............................................................... 161 
7.3. Работаем с видео ................................................................................................................... 168 
**Глава 8. Доступ к данным ................ ; ........................................................................ 170 **
8.1. Методы доступа к данным ................................................................................................... 170 
8.2. Работа с файловой системой ................................................................................................ 170 
8.3. Работаем с настройками (предпочтениями) ....................................................................... 176 
**ЧАСТЬ 111. ПОСТРОЕНИЕ СЛОЖНОГО ПРИЛОЖЕНИЯ ............................. 183 **
**Глава 9. Межпроцессное взаимодействие Аndrоid-приложений ....................... 185 **
9.1. Деятельности и намерения ................................................................................................... 185 
9.2. Режимы запуска singlelnstance и singleTask ....................................................................... 190 
9 .3. Сохранение и восстановление состояния деятельности .................................................... 191 
9.4. Передача данных между деятельностями ........................................ , .................................. 192 
**Глава 10. Потоки, службы и широковещательные приемники ......................... 194 **
10.l.Потоки ................................................................................................................................. 194 
10.1.1. Запуск потока ........................................................................................................... 194 
10.1.2. Установка приоритета потока ................................................................................. 195 
10.1.3. Отмена выполнения потока ..................................................................................... 196 
10.1.4. Обработчики RиппаЬ/е-объектов: класс Handler ................................................... 196 
10.2. Службы ................................................................................................................................ 199 
10.3. Широковещательные приемники ...................................................................................... 209 
**Глава 11. Аппаратные средства смартфона/планшета ........................................ 212 **
11.1. Датчики смартфона ............................................................................................................ 212 
11.2. Работаем с камерой ............................................................................................................ 216 
11.3. Работаем с Bluetooth ........................................................................................................... 221 
11.4. Виброзвонок ........................................................................................................................ 225 
11.5. Набор номера ...................................................................................................................... 225 
11.6. Определение номера входящего звонка ........................................................................... 225 
11.7. Получение информации о смартфоне ............................................................................... 227 
11.8. Ориентация экрана ............................................................................................................. 228 
**Глава 12. Соединение с внешним миром ................................................................ 230 **
12.1. Отправка SMS ..................................................................................................................... 230 
12.2. Работа с браузером ............................................................................................................. 232 
**б Оглавление **
**Глава 13. База данных SQLite ................................................................................... 234 **
13 .1. Введение в базы данных для Android ................................................................................ 234 
13.2. Подготовка вспомогательного класса ............................................................................... 235 
13.3. Работа с базой даm~ых ........................................................................................................ 240 
13.3.1. Создание базы данных ............................................................................................. 240 
13.3.2. Вставка записей ........................................................................................................ 241 
13.3.3. Чтение данных .......................................................................................................... 242 
**Глава 14. Создание анимации ................................................................................... 243 **
14.1. Анимация преобразований ................................................................................................. 243 
14.2. Традиционная кадровая а1П1Мация .................................................................................... 246 
**ЧАСТЬ IV. ДОПОЛНИТЕЛЬНЫЕ МАТЕРИАЛЫ ............................................. 249 **
**Глава 15. Арр Inventor - среда быстрой разработки приложений .................. 251 **
15.1. Введение в Арр Inventor ..................................................................................................... 251 
15.2. Начало работы с Арр Inventor ........................................................................................... 252 
15.3. Основной экран Арр Inventor ............................................................................................ 255 
15.4. Проектирование приложения ............................................................................................ 256 
**Глава 16. Отладка приложений ................................................................................ 260 **
16.1. Используем Android Studio ................................................................................................ 260 
16.1.1. Выбор конфигурации запуска ................................................................................. 260 
16.1.2. Запуск процесса отладки ......................................................................... ,. ............... 263 
16.1.3. Профайлинг .............................................................................................................. 264 
16.1.4. Исследуем файловую систему устройства ............................................................. 265 
16.1.5. Утилита LogCat ........................................................................................................ 266 
16.2. Утилиты отладки из Android SDK ..................................................................................... 268 
16.2.1. Android Debug Bridge ............................................................................................... 268 
16.2.2. Системные утилиты отладки ................................................................................... 268 
16.2.3. Отладчик gdb и Аndrоid-приложения ..................................................................... 269 
**Глава 17. Распространение ваших программ через Google Play ........................ 270 **
17 .1. Введение в Google Play ................................. i .................................................................... 270 
17 .2. Правила размещения приложений на Google Play ........................................................... 271 
17.3. Теория и практика .............................................................................................................. 272 
17.4. Регистрация аккаунта разработчика .................................................................................. 274 
17.5. Подготовка приложения к продаже .................................................................................. 274 
17.5.1. Тестирования на разных устройствах ..................................................................... 274 
17.5.2. Поддержка другого размера экрана ........................................................................ 275 
17.5.3. Локализация .............................................................................................................. 275 
17.5.4. Значок приложения .................................................................................................. 275 
17.5.5. Подготовка АРК-файла к загрузке ......................................................................... 275 
**Глава 18. Эмулятор Genymotion ............................................................................... 278 **
18.1. Основные сведения о Genyrnotion ..................................................................................... 278 
18.2. Создание виртуального устройства ................................................................................... 279 
Вместо заключения ..................................................................................................... 283 
Предметный указатель .............................................................................................. 284 
***Введение***
Я всегда мечтал о том, чтобы моим компьютером 
можно было пользоваться так же легко, как телефоном; моя мечта сбылась: я уже не могу разобраться, как пользоваться моим телефоном. 
Бьёрн Страуструп (Bjame Stroustrup) 
Когда-то мобильный телефон был именно мобильным телефоном: по нему можно 
было поговорить вне дома без необходимости таскать за собой многометровый 
телефонный кабель или подключать полутораметровую антенну, как на популярных лет двадцать назад радиотелефонах Senao. 
Современный мо(;>ильный телефон давно уже перестал быть просто телефоном. 
Скорее всего- это компьютер (и весьма мощный!) с функциями телефона. Да и 
называют «мобильники» теперь смартфонами - чтобы подчеркнуть тот факт, что 
в руках мы держим весьма «умное» устройство, а не просто какой-то там телефон. 
Смартфон отличается от мобильного телефона наличием полноценной развитой 
операционной системы, что облегчает жизнь как разработчикам приложений для 
мобильных телефонов, так и пользователям. Впрочем, в продаже можно встретить 
и обычные мобильные телефоны - как правило, это устройства из так называемой 
«бюджетной» категории. 
Итак, вы приобрели современный смартфон. У вас теперь есть возможность загружать и устанавливать новые мелодии, вы можете использовать его в качестве медиапроигрывателя и органайзера, Интернет и почта тоже доступны в вашем мобильном устройстве. Кроме того, вы можете расширить его функциональность путем 
установки дополнительных приложений. 
Стоп! Так всеми этими функциями обладают и обычные «продвинутые» телефоны, 
пришедшие в свое время на смену простым «говорилкам», но не носящие гордого 
названия «смартфон». В чем же разница? А в том, что при разработке мобильного 
приложения для «продвинутых>> телефонов разработчику приходится как минимум 
учитывать требования производителя того или иного телефона, а обычно - и особенности его модели. Вспомните всевозможные сервисы загрузки на телефон приложений и прочего контента: вам надо было отправить SMS на определенный номер, но SMS не пустое, а содержащее некий код, позволяющий идентифицировать 
ваш телефон. Например, 1 - для Nokia, 2 - для Samsung и т. п. Как правило, после 
в Введение 
рекламы игры или другого какого-нибудь доступного для закачки приложения размещался целый список, включающий коды не только для телефонов разных производителей, но и для разных моделей одного и того же производителя. Пользователю легко было ошибиться - случайно отправить другой код вместо требуемого. 
В итоге с его счета снимались деньги, но приложение не работало. А разработчикам приходилось писать несколько версий одного и того же приложения, чтобы 
учесть особенности всех популярных моделей мобильных телефонов. 
С появлением смартфонов все стало гораздо проще. Разработчики пишут приложения не под конкретный телефон, а под определенную мобильную операционную 
систему. А таких операционных систем гораздо меньше, чем моделей мобильных 
телефонов. Чаще всего теперь встречаются последние версии IOS (для смартфонов 
фирмы Apple) и Android. Разработка остальных мобильных ОС- вроде Windows 
Phone или Symblan OS - свернута, и устройства, работающие под их управлением, 
сейчас редкость. 
В настоящее время именно ОС Android является самой популярной мобильной операционной системой. Судите сами: если зайти в каталог, например, того же поисковика Яндекс.Маркет, где представлены смартфоны ведущих производителей 
(Samsung, Sony, НТС, Xiaomi и др.), то фильтр выбора операционной системы предоставляет лишь два варианта: Android и IOS (а это только Apple). Понятно, что 
Аndrоid-смартфонов всех производителей гораздо больше, чем актуальных моделей 
«айфонов». 
Так что теперь разработчику достаточно написать для ОС Android всего лишь одно 
приложение, и оно будет работать на всех моделях смартфонов, где она установлена. Во всяком случае, почти на всех. В настоящее время самая «древняя» версия 
Android, устройство с которой можно купить новым, - это 5.0, ну, с натяжкой, 4.4. 
Да, на том же Яндекс.Маркет можно найти модели 2014 года, которые поставлялись тогда именно с этими версиями ОС. Остальные предшествующие ее версии, 
учитывая средний срок службы смартфона (3-4 года, хотя многие пользователи 
меняют их чаще), можно считать неактуальными. Впрочем, при разработке приложения в 2019 году я бы ориентировался хотя бы на версию 5.0, как на минимальную. В своем желании обеспечить работу приложения на старых версиях Android 
будьте осторожны - не забывайте, что оно должно работать и на более новых версиях. Будет очень неприятно, когда приложение работает хорошо на 5.0, но «rлюЧИТ>> на той же 8.1. 
***Читателям книги ***
***«Программирование для Android 5.*** 
***Самоучителы> ***
Прежде всего, разрешите выразить вам благодарность за то, что в свое время вы 
купили мою книгу «Программирование для Android 5. Самоучитель». А эту книгу, 
посвященную новейшей, 9-й версии Android (9.0 Pie, она же Android Р), вы можете 
рассматривать как следующее ее издание.
