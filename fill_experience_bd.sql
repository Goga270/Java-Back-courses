DELETE FROM lesson_section;
DELETE FROM course_section;
DELETE FROM lesson_direction;
DELETE FROM course_direction;
DELETE FROM lesson_skill;
DELETE FROM course_skill;
DELETE FROM user_course;
DELETE FROM user_lesson;
DELETE FROM comments;
DELETE FROM payments;
DELETE FROM sections;
DELETE FROM directions;
DELETE FROM lessons_on_course;
DELETE FROM single_lessons;
DELETE FROM courses;
DELETE FROM skills;
DELETE FROM users;

INSERT INTO users (firstname, lastname, patronymic, number_phone, email, password, date_created,date_updated, status, role, age, number_card) 
	VALUES
	('Максим', 'Самойленко', 'Андреевич', '670606006', 'admin@mail.ru','$2a$08$TzXi8uYRX15W6qsQZ9S3VOmE3SzFKp3JweWleXSZzSpf3yOX3rHza', '2021-08-26', '2022-08-26','ACTIVE','ADMIN',25,'6452334512342'),
	('Валерий', 'Антохин', 'Андреевич', '234234234','user1@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',20,'234242342423'),
	('Анатолий', 'Косов', 'Валерьевич', '7564645754','user2@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',22,'234242342423'),
	('Станислав', 'Шушков', 'Максимович', '753458723','user3@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',23,'234242342423'),
	('Олег', 'Краснов', 'Алексеевич', '753458723','user4@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',23,'234242342423'),
	('Кирилл', 'Ходков', 'Алексеевич', '43523456745','user5@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',24,'234242342423'),
	('Иван', 'Минаков', 'Алексеевич', '43523456745','user6@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',24,'234242342423'),
	('Валерий', 'Хамидулин', 'Алексеевич', '43523456745','user7@mail.ru','$2a$08$5e1yiPIHyMl6CdoCJLCCFO1mFxYT9rJtRkYCTFwzyZ5utBE1605X.','2021-08-26','2022-08-26','ACTIVE','USER',24,'234242342423');
	
INSERT INTO skills(name)
	VALUES
	('Python'),
	('JavaScript'),
	('JUnit'),
	('java'),
	('C++'),
	('C#'),
	('Data Science'),
	('HTML/CSS'),
	('Photoshop'),
	('Corona'),
	('3ds Max'),
	('InDesign'),
	('Illustrator'),
	('AutoCAD'),
	('MS Excel'),
	('Amplitude'),
	('Power BI'),
	('Яндекс.Метрика'),
	('Telegram'),
	('Медицина');
	
INSERT INTO directions(header)
	VALUES
	('Информационные технологии'),
	('Здоровье'),
	('Социальные науки'),
	('Естественные и технические науки'),
	('Бизнес');

INSERT INTO sections(name, direction_id)
	VALUES
	('Облачные вычисления',100),
	('Безопасность',100),
	('Управление данными',100),
	('Администрирование операционных систем ',100),
	('Исскуственный интеллект', 100),
	('Питание',101),
	('Ветеринария',101),
	('Медицина',101),
	('Исследование',101),
	('Экономика',102),
	('Юриспруденция',102),
	('Медиа и коммуникации',102),
	('Психология',102),
	('Проектирование',103),
	('Химия',103),
	('Физика',103),
	('Методики исследования',103),
	('Маркетинг',104),
	('Финансы',104),
	('Основы бизнеса',104),
	('Предпринимательство',104);
	
INSERT INTO courses(name, description, skill_level, author_id, max_number_users, current_number_users, price, date_start_course, date_end_course)
	VALUES
	('Инженер-аналитик Мастер','описание', 2, 101, 200, 2, 40000,'2023-10-01','2023-12-01'),
    ('Здоровое питание','описание', 2, 102, 100, 1, 25000,'2023-10-10','2023-12-10'),
	('Повышение квалификации','описание', 3, 103, 100,0, 50000,'2023-09-10','2023-12-10'),
	('Повышение квалификации-2','описание', 3, 103, 100,0, 50000,'2023-08-10','2023-11-10');
						
INSERT INTO lessons_on_course(name,description, link_homework, link_video, time_start_lesson, access_duration_in_day, time_end_lesson, course_id)
	VALUES
	('Занятие первое - Обработка данных','description','link_for_file_with_homework','link_video_with_lesson','2023-10-01 18:00:00 +0000',10,'2023-10-01 19:30:00 +0000',100),
	('Занятие второе - Обработка данных','description','link_for_file_with_homework','link_video_with_lesson','2023-10-08 18:00:00 +0000',10,'2023-10-08 19:30:00 +0000',100),
	('Занятие третье - Обработка данных','description','link_for_file_with_homework','link_video_with_lesson','2023-10-15 18:00:00 +0000',10,'2023-10-15 19:30:00 +0000',100),
	('Занятие четвертое - Обработка данных','description','link_for_file_with_homework','link_video_with_lesson','2023-10-22 18:00:00 +0000',10,'2023-10-22 19:30:00 +0000',100),
	('Занятие пятое - Обработка данных','description','link_for_file_with_homework','link_video_with_lesson','2023-10-29 18:00:00 +0000',10,'2023-10-29 19:30:00 +0000',100),
	
	('Занятие первое - Здоровое питание','description','link_for_file_with_homework','link_video_with_lesson','2023-10-10 16:45:00 +0000',5,'2023-10-10 17:30:00 +0000',101),
	('Занятие второе -Здоровое питание','description','link_for_file_with_homework','link_video_with_lesson','2023-10-17 16:45:00 +0000',5,'2023-10-17 17:30:00 +0000',101),
	('Занятие третье - Здоровое питание','description','link_for_file_with_homework','link_video_with_lesson','2023-10-24 15:45:00 +0000',5,'2023-10-24 16:30:00 +0000',101),
	('Занятие четвертое - Здоровое питание','description','link_for_file_with_homework','link_video_with_lesson','2023-11-01 16:45:00 +0000',5,'2023-11-01 17:30:00 +0000',101),
	('Занятие пятое - Здоровое питание','description','link_for_file_with_homework','link_video_with_lesson','2023-11-08 12:30:00 +0000',5,'2023-11-08 13:30:00 +0000',101),
	
	('Занятие первое - Повышение квалификации','description','link_for_file_with_homework','link_video_with_lesson','2023-09-10 16:45:00 +0000',10,'2023-09-10 17:30:00 +0000',102),
	('Занятие второе -Повышение квалификации','description','link_for_file_with_homework','link_video_with_lesson','2023-09-17 16:45:00 +0000',10,'2023-09-17 17:30:00 +0000',102),
	('Занятие третье - Повышение квалификации','description','link_for_file_with_homework','link_video_with_lesson','2023-09-24 15:45:00 +0000',10,'2023-09-24 16:30:00 +0000',102),
	('Занятие четвертое - Повышение квалификации','description','link_for_file_with_homework','link_video_with_lesson','2023-10-01 16:45:00 +0000',10,'2023-10-01 17:30:00 +0000',102),
	('Занятие пятое - Повышение квалификации','description','link_for_file_with_homework','link_video_with_lesson','2023-10-08 12:30:00 +0000',10,'2023-10-08 13:30:00 +0000',102),
	
	('Занятие первое - Повышение квалификации-2','description','link_for_file_with_homework','link_video_with_lesson','2023-08-10 16:45:00 +0000',10,'2023-08-10 17:30:00 +0000',103),
	('Занятие второе -Повышение квалификации-2','description','link_for_file_with_homework','link_video_with_lesson','2023-08-17 16:45:00 +0000',10,'2023-08-17 17:30:00 +0000',103),
	('Занятие третье - Повышение квалификации-2','description','link_for_file_with_homework','link_video_with_lesson','2023-08-24 15:45:00 +0000',10,'2023-08-24 16:30:00 +0000',103),
	('Занятие четвертое - Повышение квалификации-2','description','link_for_file_with_homework','link_video_with_lesson','2023-09-01 16:45:00 +0000',10,'2023-09-01 17:30:00 +0000',103),
	('Занятие пятое - Повышение квалификации-2','description','link_for_file_with_homework','link_video_with_lesson','2023-09-08 12:30:00 +0000',10,'2023-09-08 13:30:00 +0000',103);
	
	
	
	

INSERT INTO single_lessons(name,description, skill_level, author_id, max_number_users, current_number_users, price, type_lesson, link_homework, link_video, time_start_lesson, time_end_lesson)
	VALUES
	('Занятие для развития финаносовой грамотности','description', 1, 103, 40, 1, 7600, 'GROUP','link_for_file_with_homework','link_video_with_lesson','2022-09-24 17:30:00 +0000','2022-09-24 19:30:00 +0000'),
	('Повышение квалификации для психологов','description', 3, 104, 1, 1, 10000,'INDIVIDUAL','link_for_file_with_homework','link_video_with_lesson', '2022-10-10 13:30:00 +0000','2022-10-10 15:30:00 +0000'),
	('Повышение квалификации для программистов','description', 2, 105, 1, 1, 10000,'INDIVIDUAL','link_for_file_with_homework','link_video_with_lesson', '2022-09-10 13:30:00 +0000','2022-09-10 15:30:00 +0000');

INSERT INTO comments(header, body, date_created, rating, author_id, course_id)
	VALUES
	('Курс хороший','body', '2022-09-02 13:30:00 +0000', 3, 106, 100),
	('Курс хороший1','body', '2022-09-10 15:30:00 +0000', 4, 107, 100),
	('Курс хороший2','body', '2022-10-01 17:30:00 +0000', 5, 105, 100);
	
INSERT INTO comments(header, body, date_created, rating, author_id, lesson_id)
	VALUES
	('useful lesson','body', '2022-09-10 15:30:00 +0000', 4, 107, 122),
	('useful lesson','body', '2022-09-10 18:30:00 +0000', 5, 106, 122);
	
INSERT INTO course_skill(course_id, skill_id)
 	VALUES
	(100,100),
	(100,117),
	(100,106),
	(100,104),
	(101,100),
	(101,119);
	
INSERT INTO lesson_skill(lesson_id, skill_id)
	VALUES
	(120,111),
	(120,116),
	(120,118),
	(121,114),
	(121,119);
	
INSERT INTO course_section(course_id, section_id)
	VALUES
	(100,100),
	(100,102),
	(101,105),
	(101,107);
	
INSERT INTO lesson_section(lesson_id, section_id)
	VALUES
	(120,118),
	(120,117),
	(121,112),
	(121,107);
	
INSERT INTO course_direction(course_id, direction_id)
	VALUES
	(100,100),
	(101,101);
	
INSERT INTO lesson_direction(lesson_id, direction_id)
	VALUES
	(120,104),
	(121,102),
	(121,101);
	
INSERT INTO user_course(user_id, course_id)
	VALUES
	(106,100),
	(107,100),
	(105,101),
	(107,102);
	
INSERT INTO user_lesson(user_id, lesson_id)
	VALUES
	(105, 120),
	(106, 121);
	
INSERT INTO payments(costumer_id, course_id, date_payment, price)
	VALUES
	(106, 100, '2022-09-10', 40500),
	(107, 100, '2022-09-11', 40000),
	(107, 102, '2022-09-08', 80000),
	(105, 101, '2022-09-08', 25000);
	
INSERT INTO payments(costumer_id, lesson_id, date_payment, price)
	VALUES
	(105, 120, '2022-09-10', 10000),
	(106, 121, '2022-09-10', 10000);
	
	