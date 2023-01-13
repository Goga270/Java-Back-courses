DROP TABLE IF EXISTS lesson_section CASCADE;
DROP TABLE IF EXISTS course_section CASCADE;
DROP TABLE IF EXISTS lesson_direction CASCADE;
DROP TABLE IF EXISTS course_direction CASCADE;
DROP TABLE IF EXISTS lesson_skill CASCADE;
DROP TABLE IF EXISTS course_skill CASCADE;
DROP TABLE IF EXISTS user_course CASCADE;
DROP TABLE IF EXISTS user_lesson CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS sections CASCADE;
DROP TABLE IF EXISTS directions CASCADE;
DROP TABLE IF EXISTS lessons_on_course CASCADE;
DROP TABLE IF EXISTS single_lessons;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP SEQUENCE IF EXISTS sequence_id_courses;
DROP SEQUENCE IF EXISTS sequence_id_lessons;
DROP SEQUENCE IF EXISTS sequence_id_skills;
DROP SEQUENCE IF EXISTS sequence_id_users;
DROP SEQUENCE IF EXISTS sequence_id_comments;
DROP SEQUENCE IF EXISTS sequence_id_payments;
DROP SEQUENCE IF EXISTS sequence_id_directions;
DROP SEQUENCE IF EXISTS sequence_id_sections;

CREATE SEQUENCE IF NOT EXISTS sequence_id_courses start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_lessons start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_skills start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_users  start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_comments  start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_directions  start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_sections  start with 100 increment by 1;
CREATE SEQUENCE IF NOT EXISTS sequence_id_payments start with 100 increment by 1;

CREATE TABLE users (
	id integer DEFAULT nextval('sequence_id_users') PRIMARY KEY,
	firstname text NOT NULL,
	lastName text NOT NULL,
	patronymic text NOT NULL,
	number_phone varchar(20) NOT NULL,
	email text NOT NULL,
	password text NOT NULL,
	date_created date NOT NULL,
	date_updated date NOT NULL, 
	status text NOT NULL,
	role text NOT NULL,
	age smallint NOT NULL,
	number_card text NOT NULL
);

CREATE TABLE skills(
	id integer DEFAULT nextval('sequence_id_skills') PRIMARY KEY,
	name text NOT NULL
);

CREATE TABLE directions(
	id integer DEFAULT nextval('sequence_id_directions') PRIMARY KEY,
	header text NOT NULL
);

CREATE TABLE sections(
	id integer DEFAULT nextval('sequence_id_sections') PRIMARY KEY,
	name text NOT NULL, 
	direction_id integer NULL,
	CONSTRAINT FK_direction
		FOREIGN KEY(direction_id) REFERENCES directions(id) ON DELETE CASCADE
);
	
CREATE TABLE courses(
	id integer DEFAULT nextval('sequence_id_courses') PRIMARY KEY,
	name text NOT NULL,
	description text NOT NULL,
	skill_level integer NOT NULL,
	author_id integer NOT NULL,
	max_number_users integer NOT NULL,
	price numeric(12,4) NOT NULL,
	current_number_users integer DEFAULT 0,
	date_start_course date NOT NULL,
	date_end_course date NOT NULL,
	CONSTRAINT PK_user
		FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
); 
CREATE TABLE lessons_on_course (
	id integer DEFAULT nextval('sequence_id_lessons') PRIMARY KEY,
	name text NOT NULL,
	description text NOT NULL,
	link_homework text,
	link_video text,
	time_start_lesson timestamptz NOT NULL,
	time_end_lesson timestamptz NOT NULL,
	access_duration_in_day integer NOT NULL,
	course_id integer  not NULL,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE
);
CREATE TABLE single_lessons (
	id integer DEFAULT nextval('sequence_id_lessons') PRIMARY KEY,
	name text NOT NULL,
	description text NOT NULL,
	skill_level integer NOT NULL,
	author_id integer NOT NULL,
	max_number_users integer NOT NULL,
	price numeric(12,4) NOT NULL,
	current_number_users integer,
	type_lesson text NOT NULL,
	link_homework text NOT NULL,
	link_video text NOT NULL,
	time_start_lesson timestamptz NOT NULL,
	time_end_lesson timestamptz NOT NULL,
	CONSTRAINT PK_user
		FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
); 

CREATE TABLE comments (
	id integer DEFAULT nextval('sequence_id_comments') PRIMARY KEY,
	header text NOT NULL, 
	body text NOT NULL, 
	date_created timestamptz NOT NULL,
	author_id integer NOT NULL,
	rating integer NOT NULL,
	course_id integer, 
	lesson_id integer, 
	CONSTRAINT FK_user_author
		FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE
);

CREATE TABLE payments (
	id integer DEFAULT nextval('sequence_id_payments') PRIMARY KEY,
	costumer_id integer NOT NULL,
	course_id integer NULL,
	lesson_id integer NULL,
	date_payment timestamptz NOT NULL,
	price numeric(12,4) NOT NULL,
	CONSTRAINT FK_user_author
		FOREIGN KEY(costumer_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE
);


CREATE TABLE course_skill (
	course_id integer NOT NULL,
	skill_id integer NOT NULL,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_skill
		FOREIGN KEY(skill_id) REFERENCES skills(id) ON DELETE CASCADE,
	CONSTRAINT PK_course_skill
		PRIMARY KEY (course_id, skill_id)
);

CREATE TABLE lesson_skill (
	lesson_id integer NOT NULL,
	skill_id integer NOT NULL,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE,
	CONSTRAINT FK_skill
		FOREIGN KEY(skill_id) REFERENCES skills(id) ON DELETE CASCADE,
	CONSTRAINT PK_lesson_skill
		PRIMARY KEY (lesson_id, skill_id)
);

CREATE TABLE course_section (
	course_id integer NOT NULL,
	section_id integer NOT NULL,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_section
		FOREIGN KEY(section_id) REFERENCES sections(id) ON DELETE CASCADE,
	CONSTRAINT PK_course_section
		PRIMARY KEY (course_id, section_id)
);

CREATE TABLE lesson_section (
	lesson_id integer NOT NULL,
	section_id integer NOT NULL,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE,
	CONSTRAINT FK_section
		FOREIGN KEY(section_id) REFERENCES sections(id) ON DELETE CASCADE,
	CONSTRAINT PK_lesson_section
		PRIMARY KEY (lesson_id, section_id)
);

CREATE TABLE lesson_direction (
	lesson_id integer NOT NULL,
	direction_id integer NOT NULL,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE,
	CONSTRAINT FK_direction
		FOREIGN KEY(direction_id) REFERENCES directions(id) ON DELETE CASCADE,
	CONSTRAINT PK_lesson_direction
		PRIMARY KEY (lesson_id, direction_id)
);

CREATE TABLE course_direction (
	course_id integer NOT NULL,
	direction_id integer NOT NULL,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_direction
		FOREIGN KEY(direction_id) REFERENCES directions(id) ON DELETE CASCADE,
	CONSTRAINT PK_course_direction
		PRIMARY KEY (course_id, direction_id)
);

CREATE TABLE user_course (
	user_id integer NOT NULL,
	course_id integer NOT NULL,
	CONSTRAINT FK_course 
		FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,
	CONSTRAINT FK_user
		FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT PK_user_course
		PRIMARY KEY (course_id, user_id)
);

CREATE TABLE user_lesson (
	user_id integer NOT NULL,
	lesson_id integer NOT NULL,
	CONSTRAINT FK_lesson
		FOREIGN KEY(lesson_id) REFERENCES single_lessons(id) ON DELETE CASCADE,
	CONSTRAINT FK_user
		FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT PK_user_lesson
		PRIMARY KEY (lesson_id, user_id)
);
