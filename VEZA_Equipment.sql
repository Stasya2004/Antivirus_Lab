--
-- PostgreSQL database cluster dump
--

-- Started on 2025-12-27 08:28:45

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:VQrAB8BtTcxui7tE0OeGfA==$9kAFeNlD/XNLS4MCSAg4Ty1n0+0c4vV2yPdW6qPBvWQ=:lA5WiPD1dBbXpcuI0ONkz5GpMgx4QYnk0prx1VxZa5M=';

--
-- User Configurations
--








--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.0
-- Dumped by pg_dump version 15.2

-- Started on 2025-12-27 08:28:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Completed on 2025-12-27 08:28:45

--
-- PostgreSQL database dump complete
--

--
-- Database "postgres" dump
--

\connect postgres

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.0
-- Dumped by pg_dump version 15.2

-- Started on 2025-12-27 08:28:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 45938)
-- Name: common; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA common;


ALTER SCHEMA common OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 45940)
-- Name: doc; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA doc;


ALTER SCHEMA doc OWNER TO postgres;

--
-- TOC entry 11 (class 2615 OID 45942)
-- Name: equipment; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA equipment;


ALTER SCHEMA equipment OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 45939)
-- Name: img; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA img;


ALTER SCHEMA img OWNER TO postgres;

--
-- TOC entry 10 (class 2615 OID 45941)
-- Name: industry; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA industry;


ALTER SCHEMA industry OWNER TO postgres;

--
-- TOC entry 2 (class 3079 OID 16384)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 893 (class 1247 OID 45944)
-- Name: statistics_type_enum; Type: TYPE; Schema: common; Owner: postgres
--

CREATE TYPE common.statistics_type_enum AS ENUM (
    'industry',
    'equipment',
    'doc'
);


ALTER TYPE common.statistics_type_enum OWNER TO postgres;

--
-- TOC entry 896 (class 1247 OID 45952)
-- Name: status_enum; Type: TYPE; Schema: common; Owner: postgres
--

CREATE TYPE common.status_enum AS ENUM (
    'active',
    'archived'
);


ALTER TYPE common.status_enum OWNER TO postgres;

--
-- TOC entry 902 (class 1247 OID 45974)
-- Name: category_enum; Type: TYPE; Schema: doc; Owner: postgres
--

CREATE TYPE doc.category_enum AS ENUM (
    'equipment',
    'size_discription',
    'size_technical',
    'equipment_doc'
);


ALTER TYPE doc.category_enum OWNER TO postgres;

--
-- TOC entry 899 (class 1247 OID 45958)
-- Name: category_enum; Type: TYPE; Schema: img; Owner: postgres
--

CREATE TYPE img.category_enum AS ENUM (
    'industry',
    'object',
    'equipment_gallary',
    'equipment_category',
    'equipment_size',
    'sertificate',
    'size_gallary'
);


ALTER TYPE img.category_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 45984)
-- Name: statistics; Type: TABLE; Schema: common; Owner: postgres
--

CREATE TABLE common.statistics (
    id bigint NOT NULL,
    statistics bigint,
    date timestamp without time zone DEFAULT now(),
    type common.statistics_type_enum
);


ALTER TABLE common.statistics OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 45983)
-- Name: statistics_id_seq; Type: SEQUENCE; Schema: common; Owner: postgres
--

CREATE SEQUENCE common.statistics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE common.statistics_id_seq OWNER TO postgres;

--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 220
-- Name: statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: common; Owner: postgres
--

ALTER SEQUENCE common.statistics_id_seq OWNED BY common.statistics.id;


--
-- TOC entry 241 (class 1259 OID 46060)
-- Name: additional_equipment; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.additional_equipment (
    id bigint NOT NULL,
    equipment_id integer,
    main_equipment_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.additional_equipment OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 46059)
-- Name: additional_equipment_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.additional_equipment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.additional_equipment_id_seq OWNER TO postgres;

--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 240
-- Name: additional_equipment_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.additional_equipment_id_seq OWNED BY equipment.additional_equipment.id;


--
-- TOC entry 239 (class 1259 OID 46052)
-- Name: advantage; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.advantage (
    id bigint NOT NULL,
    title character varying(100),
    equipment_id bigint NOT NULL
);


ALTER TABLE equipment.advantage OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 46051)
-- Name: advantage_equipment_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.advantage_equipment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.advantage_equipment_id_seq OWNER TO postgres;

--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 238
-- Name: advantage_equipment_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.advantage_equipment_id_seq OWNED BY equipment.advantage.equipment_id;


--
-- TOC entry 237 (class 1259 OID 46050)
-- Name: advantage_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.advantage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.advantage_id_seq OWNER TO postgres;

--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 237
-- Name: advantage_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.advantage_id_seq OWNED BY equipment.advantage.id;


--
-- TOC entry 251 (class 1259 OID 46101)
-- Name: block; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.block (
    id bigint NOT NULL,
    title character varying(200),
    category doc.category_enum,
    equipment_id integer,
    size_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.block OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 46100)
-- Name: block_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.block_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.block_id_seq OWNER TO postgres;

--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 250
-- Name: block_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.block_id_seq OWNED BY equipment.block.id;


--
-- TOC entry 233 (class 1259 OID 46031)
-- Name: category; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.category (
    id smallint NOT NULL,
    title character varying(200),
    seo character varying(200),
    alias character varying(200),
    img_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.category OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 46030)
-- Name: category_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.category_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.category_id_seq OWNER TO postgres;

--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 232
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.category_id_seq OWNED BY equipment.category.id;


--
-- TOC entry 253 (class 1259 OID 46108)
-- Name: doc; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.doc (
    id bigint NOT NULL,
    title character varying(200),
    "docURL" character varying(100),
    block_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.doc OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 46107)
-- Name: doc_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.doc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.doc_id_seq OWNER TO postgres;

--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 252
-- Name: doc_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.doc_id_seq OWNED BY equipment.doc.id;


--
-- TOC entry 236 (class 1259 OID 46041)
-- Name: equipment; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.equipment (
    id bigint NOT NULL,
    title character varying(200),
    description character varying(1000),
    short_description character varying(500),
    seo character varying(200),
    alias character varying(200),
    subtitle character varying(200),
    type_id smallint,
    user_id bigint NOT NULL,
    status common.status_enum
);


ALTER TABLE equipment.equipment OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 46039)
-- Name: equipment_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.equipment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.equipment_id_seq OWNER TO postgres;

--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 234
-- Name: equipment_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.equipment_id_seq OWNED BY equipment.equipment.id;


--
-- TOC entry 235 (class 1259 OID 46040)
-- Name: equipment_user_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.equipment_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.equipment_user_id_seq OWNER TO postgres;

--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 235
-- Name: equipment_user_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.equipment_user_id_seq OWNED BY equipment.equipment.user_id;


--
-- TOC entry 243 (class 1259 OID 46067)
-- Name: main_equipment; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.main_equipment (
    id bigint NOT NULL,
    equipment_id integer,
    status common.status_enum
);


ALTER TABLE equipment.main_equipment OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 46066)
-- Name: main_equipment_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.main_equipment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.main_equipment_id_seq OWNER TO postgres;

--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 242
-- Name: main_equipment_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.main_equipment_id_seq OWNED BY equipment.main_equipment.id;


--
-- TOC entry 255 (class 1259 OID 46115)
-- Name: sertificate; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.sertificate (
    id bigint NOT NULL,
    title character varying(200),
    "sertificateURL" character varying(200),
    equipment_id bigint,
    update timestamp without time zone,
    status common.status_enum
);


ALTER TABLE equipment.sertificate OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 46122)
-- Name: sertificate_category; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.sertificate_category (
    id bigint NOT NULL,
    title character varying(200),
    img_id bigint,
    category_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.sertificate_category OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 46121)
-- Name: sertificate_category_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.sertificate_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.sertificate_category_id_seq OWNER TO postgres;

--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 256
-- Name: sertificate_category_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.sertificate_category_id_seq OWNED BY equipment.sertificate_category.id;


--
-- TOC entry 254 (class 1259 OID 46114)
-- Name: sertificate_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.sertificate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.sertificate_id_seq OWNER TO postgres;

--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 254
-- Name: sertificate_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.sertificate_id_seq OWNED BY equipment.sertificate.id;


--
-- TOC entry 249 (class 1259 OID 46092)
-- Name: size; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.size (
    id bigint NOT NULL,
    title character varying(200),
    seo character varying(200),
    alias character varying(200),
    equipment_id bigint,
    status common.status_enum
);


ALTER TABLE equipment.size OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 46091)
-- Name: size_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.size_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.size_id_seq OWNER TO postgres;

--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 248
-- Name: size_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.size_id_seq OWNED BY equipment.size.id;


--
-- TOC entry 245 (class 1259 OID 46074)
-- Name: subcategory; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.subcategory (
    id smallint NOT NULL,
    title character varying(100),
    seo character varying(200),
    alias character varying(200),
    category_id smallint,
    status common.status_enum
);


ALTER TABLE equipment.subcategory OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 46073)
-- Name: subcategory_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.subcategory_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.subcategory_id_seq OWNER TO postgres;

--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 244
-- Name: subcategory_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.subcategory_id_seq OWNED BY equipment.subcategory.id;


--
-- TOC entry 247 (class 1259 OID 46083)
-- Name: type; Type: TABLE; Schema: equipment; Owner: postgres
--

CREATE TABLE equipment.type (
    id bigint NOT NULL,
    title character varying(100),
    seo character varying(200),
    alias character varying(200),
    subcategory_id smallint,
    status common.status_enum
);


ALTER TABLE equipment.type OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 46082)
-- Name: type_id_seq; Type: SEQUENCE; Schema: equipment; Owner: postgres
--

CREATE SEQUENCE equipment.type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE equipment.type_id_seq OWNER TO postgres;

--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 246
-- Name: type_id_seq; Type: SEQUENCE OWNED BY; Schema: equipment; Owner: postgres
--

ALTER SEQUENCE equipment.type_id_seq OWNED BY equipment.type.id;


--
-- TOC entry 223 (class 1259 OID 45992)
-- Name: img; Type: TABLE; Schema: img; Owner: postgres
--

CREATE TABLE img.img (
    id bigint NOT NULL,
    title character varying(200),
    "img_URL" character varying(200),
    category img.category_enum,
    object_id bigint,
    equipment_id bigint,
    size_id bigint
);


ALTER TABLE img.img OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 45991)
-- Name: img_id_seq; Type: SEQUENCE; Schema: img; Owner: postgres
--

CREATE SEQUENCE img.img_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE img.img_id_seq OWNER TO postgres;

--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 222
-- Name: img_id_seq; Type: SEQUENCE OWNED BY; Schema: img; Owner: postgres
--

ALTER SEQUENCE img.img_id_seq OWNED BY img.img.id;


--
-- TOC entry 229 (class 1259 OID 46017)
-- Name: equipment_industry; Type: TABLE; Schema: industry; Owner: postgres
--

CREATE TABLE industry.equipment_industry (
    id bigint NOT NULL,
    industry_id smallint,
    equipment_category_id integer
);


ALTER TABLE industry.equipment_industry OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 46016)
-- Name: equipment_industry_id_seq; Type: SEQUENCE; Schema: industry; Owner: postgres
--

CREATE SEQUENCE industry.equipment_industry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE industry.equipment_industry_id_seq OWNER TO postgres;

--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 228
-- Name: equipment_industry_id_seq; Type: SEQUENCE OWNED BY; Schema: industry; Owner: postgres
--

ALTER SEQUENCE industry.equipment_industry_id_seq OWNED BY industry.equipment_industry.id;


--
-- TOC entry 225 (class 1259 OID 45999)
-- Name: industry; Type: TABLE; Schema: industry; Owner: postgres
--

CREATE TABLE industry.industry (
    id smallint NOT NULL,
    title character varying(200),
    description character varying(1000),
    short_description character varying(500),
    seo character varying(200),
    img_id bigint,
    alias character varying(200),
    status common.status_enum
);


ALTER TABLE industry.industry OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 45998)
-- Name: industry_id_seq; Type: SEQUENCE; Schema: industry; Owner: postgres
--

CREATE SEQUENCE industry.industry_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE industry.industry_id_seq OWNER TO postgres;

--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 224
-- Name: industry_id_seq; Type: SEQUENCE OWNED BY; Schema: industry; Owner: postgres
--

ALTER SEQUENCE industry.industry_id_seq OWNED BY industry.industry.id;


--
-- TOC entry 227 (class 1259 OID 46008)
-- Name: object; Type: TABLE; Schema: industry; Owner: postgres
--

CREATE TABLE industry.object (
    id smallint NOT NULL,
    title character varying(200),
    title_delivery character varying(200),
    address character varying(200),
    industry_id smallint,
    slide_position smallint NOT NULL,
    status common.status_enum
);


ALTER TABLE industry.object OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 46024)
-- Name: object_equipment_category; Type: TABLE; Schema: industry; Owner: postgres
--

CREATE TABLE industry.object_equipment_category (
    id bigint NOT NULL,
    object_id bigint,
    equipment_id bigint
);


ALTER TABLE industry.object_equipment_category OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 46023)
-- Name: object_equipment_category_id_seq; Type: SEQUENCE; Schema: industry; Owner: postgres
--

CREATE SEQUENCE industry.object_equipment_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE industry.object_equipment_category_id_seq OWNER TO postgres;

--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 230
-- Name: object_equipment_category_id_seq; Type: SEQUENCE OWNED BY; Schema: industry; Owner: postgres
--

ALTER SEQUENCE industry.object_equipment_category_id_seq OWNED BY industry.object_equipment_category.id;


--
-- TOC entry 226 (class 1259 OID 46007)
-- Name: object_id_seq; Type: SEQUENCE; Schema: industry; Owner: postgres
--

CREATE SEQUENCE industry.object_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE industry.object_id_seq OWNER TO postgres;

--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 226
-- Name: object_id_seq; Type: SEQUENCE OWNED BY; Schema: industry; Owner: postgres
--

ALTER SEQUENCE industry.object_id_seq OWNED BY industry.object.id;


--
-- TOC entry 259 (class 1259 OID 75802)
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id bigint NOT NULL,
    text character varying(255),
    project_id bigint,
    task_id bigint,
    user_id bigint
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 75801)
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.comments ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 260 (class 1259 OID 75807)
-- Name: project_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_users (
    project_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.project_users OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 75811)
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projects (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE public.projects OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 75810)
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.projects ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 264 (class 1259 OID 75819)
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.tags OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 75818)
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tags ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 265 (class 1259 OID 75824)
-- Name: task_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_tags (
    task_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.task_tags OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 75827)
-- Name: task_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_users (
    task_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.task_users OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 75831)
-- Name: tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255),
    status character varying(255),
    project_id bigint,
    CONSTRAINT tasks_status_check CHECK (((status)::text = ANY ((ARRAY['IN_PROGRESS'::character varying, 'REVIEW'::character varying, 'OPEN'::character varying, 'DONE'::character varying])::text[])))
);


ALTER TABLE public.tasks OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 75830)
-- Name: tasks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.tasks ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tasks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 270 (class 1259 OID 75840)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 75839)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3315 (class 2604 OID 45987)
-- Name: statistics id; Type: DEFAULT; Schema: common; Owner: postgres
--

ALTER TABLE ONLY common.statistics ALTER COLUMN id SET DEFAULT nextval('common.statistics_id_seq'::regclass);


--
-- TOC entry 3327 (class 2604 OID 46063)
-- Name: additional_equipment id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.additional_equipment ALTER COLUMN id SET DEFAULT nextval('equipment.additional_equipment_id_seq'::regclass);


--
-- TOC entry 3325 (class 2604 OID 46055)
-- Name: advantage id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.advantage ALTER COLUMN id SET DEFAULT nextval('equipment.advantage_id_seq'::regclass);


--
-- TOC entry 3326 (class 2604 OID 46056)
-- Name: advantage equipment_id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.advantage ALTER COLUMN equipment_id SET DEFAULT nextval('equipment.advantage_equipment_id_seq'::regclass);


--
-- TOC entry 3332 (class 2604 OID 46104)
-- Name: block id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.block ALTER COLUMN id SET DEFAULT nextval('equipment.block_id_seq'::regclass);


--
-- TOC entry 3322 (class 2604 OID 46034)
-- Name: category id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.category ALTER COLUMN id SET DEFAULT nextval('equipment.category_id_seq'::regclass);


--
-- TOC entry 3333 (class 2604 OID 46111)
-- Name: doc id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.doc ALTER COLUMN id SET DEFAULT nextval('equipment.doc_id_seq'::regclass);


--
-- TOC entry 3323 (class 2604 OID 46044)
-- Name: equipment id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.equipment ALTER COLUMN id SET DEFAULT nextval('equipment.equipment_id_seq'::regclass);


--
-- TOC entry 3324 (class 2604 OID 46045)
-- Name: equipment user_id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.equipment ALTER COLUMN user_id SET DEFAULT nextval('equipment.equipment_user_id_seq'::regclass);


--
-- TOC entry 3328 (class 2604 OID 46070)
-- Name: main_equipment id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.main_equipment ALTER COLUMN id SET DEFAULT nextval('equipment.main_equipment_id_seq'::regclass);


--
-- TOC entry 3334 (class 2604 OID 46118)
-- Name: sertificate id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate ALTER COLUMN id SET DEFAULT nextval('equipment.sertificate_id_seq'::regclass);


--
-- TOC entry 3335 (class 2604 OID 46125)
-- Name: sertificate_category id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate_category ALTER COLUMN id SET DEFAULT nextval('equipment.sertificate_category_id_seq'::regclass);


--
-- TOC entry 3331 (class 2604 OID 46095)
-- Name: size id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.size ALTER COLUMN id SET DEFAULT nextval('equipment.size_id_seq'::regclass);


--
-- TOC entry 3329 (class 2604 OID 46077)
-- Name: subcategory id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.subcategory ALTER COLUMN id SET DEFAULT nextval('equipment.subcategory_id_seq'::regclass);


--
-- TOC entry 3330 (class 2604 OID 46086)
-- Name: type id; Type: DEFAULT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.type ALTER COLUMN id SET DEFAULT nextval('equipment.type_id_seq'::regclass);


--
-- TOC entry 3317 (class 2604 OID 45995)
-- Name: img id; Type: DEFAULT; Schema: img; Owner: postgres
--

ALTER TABLE ONLY img.img ALTER COLUMN id SET DEFAULT nextval('img.img_id_seq'::regclass);


--
-- TOC entry 3320 (class 2604 OID 46020)
-- Name: equipment_industry id; Type: DEFAULT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.equipment_industry ALTER COLUMN id SET DEFAULT nextval('industry.equipment_industry_id_seq'::regclass);


--
-- TOC entry 3318 (class 2604 OID 46002)
-- Name: industry id; Type: DEFAULT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.industry ALTER COLUMN id SET DEFAULT nextval('industry.industry_id_seq'::regclass);


--
-- TOC entry 3319 (class 2604 OID 46011)
-- Name: object id; Type: DEFAULT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object ALTER COLUMN id SET DEFAULT nextval('industry.object_id_seq'::regclass);


--
-- TOC entry 3321 (class 2604 OID 46027)
-- Name: object_equipment_category id; Type: DEFAULT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object_equipment_category ALTER COLUMN id SET DEFAULT nextval('industry.object_equipment_category_id_seq'::regclass);


--
-- TOC entry 3560 (class 0 OID 45984)
-- Dependencies: 221
-- Data for Name: statistics; Type: TABLE DATA; Schema: common; Owner: postgres
--

COPY common.statistics (id, statistics, date, type) FROM stdin;
\.


--
-- TOC entry 3580 (class 0 OID 46060)
-- Dependencies: 241
-- Data for Name: additional_equipment; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.additional_equipment (id, equipment_id, main_equipment_id, status) FROM stdin;
\.


--
-- TOC entry 3578 (class 0 OID 46052)
-- Dependencies: 239
-- Data for Name: advantage; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.advantage (id, title, equipment_id) FROM stdin;
\.


--
-- TOC entry 3590 (class 0 OID 46101)
-- Dependencies: 251
-- Data for Name: block; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.block (id, title, category, equipment_id, size_id, status) FROM stdin;
\.


--
-- TOC entry 3572 (class 0 OID 46031)
-- Dependencies: 233
-- Data for Name: category; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.category (id, title, seo, alias, img_id, status) FROM stdin;
\.


--
-- TOC entry 3592 (class 0 OID 46108)
-- Dependencies: 253
-- Data for Name: doc; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.doc (id, title, "docURL", block_id, status) FROM stdin;
\.


--
-- TOC entry 3575 (class 0 OID 46041)
-- Dependencies: 236
-- Data for Name: equipment; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.equipment (id, title, description, short_description, seo, alias, subtitle, type_id, user_id, status) FROM stdin;
\.


--
-- TOC entry 3582 (class 0 OID 46067)
-- Dependencies: 243
-- Data for Name: main_equipment; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.main_equipment (id, equipment_id, status) FROM stdin;
\.


--
-- TOC entry 3594 (class 0 OID 46115)
-- Dependencies: 255
-- Data for Name: sertificate; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.sertificate (id, title, "sertificateURL", equipment_id, update, status) FROM stdin;
\.


--
-- TOC entry 3596 (class 0 OID 46122)
-- Dependencies: 257
-- Data for Name: sertificate_category; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.sertificate_category (id, title, img_id, category_id, status) FROM stdin;
\.


--
-- TOC entry 3588 (class 0 OID 46092)
-- Dependencies: 249
-- Data for Name: size; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.size (id, title, seo, alias, equipment_id, status) FROM stdin;
\.


--
-- TOC entry 3584 (class 0 OID 46074)
-- Dependencies: 245
-- Data for Name: subcategory; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.subcategory (id, title, seo, alias, category_id, status) FROM stdin;
\.


--
-- TOC entry 3586 (class 0 OID 46083)
-- Dependencies: 247
-- Data for Name: type; Type: TABLE DATA; Schema: equipment; Owner: postgres
--

COPY equipment.type (id, title, seo, alias, subcategory_id, status) FROM stdin;
\.


--
-- TOC entry 3562 (class 0 OID 45992)
-- Dependencies: 223
-- Data for Name: img; Type: TABLE DATA; Schema: img; Owner: postgres
--

COPY img.img (id, title, "img_URL", category, object_id, equipment_id, size_id) FROM stdin;
\.


--
-- TOC entry 3568 (class 0 OID 46017)
-- Dependencies: 229
-- Data for Name: equipment_industry; Type: TABLE DATA; Schema: industry; Owner: postgres
--

COPY industry.equipment_industry (id, industry_id, equipment_category_id) FROM stdin;
\.


--
-- TOC entry 3564 (class 0 OID 45999)
-- Dependencies: 225
-- Data for Name: industry; Type: TABLE DATA; Schema: industry; Owner: postgres
--

COPY industry.industry (id, title, description, short_description, seo, img_id, alias, status) FROM stdin;
\.


--
-- TOC entry 3566 (class 0 OID 46008)
-- Dependencies: 227
-- Data for Name: object; Type: TABLE DATA; Schema: industry; Owner: postgres
--

COPY industry.object (id, title, title_delivery, address, industry_id, slide_position, status) FROM stdin;
\.


--
-- TOC entry 3570 (class 0 OID 46024)
-- Dependencies: 231
-- Data for Name: object_equipment_category; Type: TABLE DATA; Schema: industry; Owner: postgres
--

COPY industry.object_equipment_category (id, object_id, equipment_id) FROM stdin;
\.


--
-- TOC entry 3598 (class 0 OID 75802)
-- Dependencies: 259
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, text, project_id, task_id, user_id) FROM stdin;
\.


--
-- TOC entry 3599 (class 0 OID 75807)
-- Dependencies: 260
-- Data for Name: project_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_users (project_id, user_id) FROM stdin;
\.


--
-- TOC entry 3601 (class 0 OID 75811)
-- Dependencies: 262
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.projects (id, description, name) FROM stdin;
\.


--
-- TOC entry 3603 (class 0 OID 75819)
-- Dependencies: 264
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tags (id, name) FROM stdin;
\.


--
-- TOC entry 3604 (class 0 OID 75824)
-- Dependencies: 265
-- Data for Name: task_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_tags (task_id, tag_id) FROM stdin;
\.


--
-- TOC entry 3605 (class 0 OID 75827)
-- Dependencies: 266
-- Data for Name: task_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_users (task_id, user_id) FROM stdin;
\.


--
-- TOC entry 3607 (class 0 OID 75831)
-- Dependencies: 268
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tasks (id, description, name, status, project_id) FROM stdin;
\.


--
-- TOC entry 3609 (class 0 OID 75840)
-- Dependencies: 270
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name) FROM stdin;
1	Анастасия
2	Иван
\.


--
-- TOC entry 3636 (class 0 OID 0)
-- Dependencies: 220
-- Name: statistics_id_seq; Type: SEQUENCE SET; Schema: common; Owner: postgres
--

SELECT pg_catalog.setval('common.statistics_id_seq', 1, false);


--
-- TOC entry 3637 (class 0 OID 0)
-- Dependencies: 240
-- Name: additional_equipment_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.additional_equipment_id_seq', 1, false);


--
-- TOC entry 3638 (class 0 OID 0)
-- Dependencies: 238
-- Name: advantage_equipment_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.advantage_equipment_id_seq', 1, false);


--
-- TOC entry 3639 (class 0 OID 0)
-- Dependencies: 237
-- Name: advantage_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.advantage_id_seq', 1, false);


--
-- TOC entry 3640 (class 0 OID 0)
-- Dependencies: 250
-- Name: block_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.block_id_seq', 1, false);


--
-- TOC entry 3641 (class 0 OID 0)
-- Dependencies: 232
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.category_id_seq', 1, false);


--
-- TOC entry 3642 (class 0 OID 0)
-- Dependencies: 252
-- Name: doc_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.doc_id_seq', 1, false);


--
-- TOC entry 3643 (class 0 OID 0)
-- Dependencies: 234
-- Name: equipment_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.equipment_id_seq', 1, false);


--
-- TOC entry 3644 (class 0 OID 0)
-- Dependencies: 235
-- Name: equipment_user_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.equipment_user_id_seq', 1, false);


--
-- TOC entry 3645 (class 0 OID 0)
-- Dependencies: 242
-- Name: main_equipment_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.main_equipment_id_seq', 1, false);


--
-- TOC entry 3646 (class 0 OID 0)
-- Dependencies: 256
-- Name: sertificate_category_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.sertificate_category_id_seq', 1, false);


--
-- TOC entry 3647 (class 0 OID 0)
-- Dependencies: 254
-- Name: sertificate_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.sertificate_id_seq', 1, false);


--
-- TOC entry 3648 (class 0 OID 0)
-- Dependencies: 248
-- Name: size_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.size_id_seq', 1, false);


--
-- TOC entry 3649 (class 0 OID 0)
-- Dependencies: 244
-- Name: subcategory_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.subcategory_id_seq', 1, false);


--
-- TOC entry 3650 (class 0 OID 0)
-- Dependencies: 246
-- Name: type_id_seq; Type: SEQUENCE SET; Schema: equipment; Owner: postgres
--

SELECT pg_catalog.setval('equipment.type_id_seq', 1, false);


--
-- TOC entry 3651 (class 0 OID 0)
-- Dependencies: 222
-- Name: img_id_seq; Type: SEQUENCE SET; Schema: img; Owner: postgres
--

SELECT pg_catalog.setval('img.img_id_seq', 1, false);


--
-- TOC entry 3652 (class 0 OID 0)
-- Dependencies: 228
-- Name: equipment_industry_id_seq; Type: SEQUENCE SET; Schema: industry; Owner: postgres
--

SELECT pg_catalog.setval('industry.equipment_industry_id_seq', 1, false);


--
-- TOC entry 3653 (class 0 OID 0)
-- Dependencies: 224
-- Name: industry_id_seq; Type: SEQUENCE SET; Schema: industry; Owner: postgres
--

SELECT pg_catalog.setval('industry.industry_id_seq', 1, false);


--
-- TOC entry 3654 (class 0 OID 0)
-- Dependencies: 230
-- Name: object_equipment_category_id_seq; Type: SEQUENCE SET; Schema: industry; Owner: postgres
--

SELECT pg_catalog.setval('industry.object_equipment_category_id_seq', 1, false);


--
-- TOC entry 3655 (class 0 OID 0)
-- Dependencies: 226
-- Name: object_id_seq; Type: SEQUENCE SET; Schema: industry; Owner: postgres
--

SELECT pg_catalog.setval('industry.object_id_seq', 1, false);


--
-- TOC entry 3656 (class 0 OID 0)
-- Dependencies: 258
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_id_seq', 1, false);


--
-- TOC entry 3657 (class 0 OID 0)
-- Dependencies: 261
-- Name: projects_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.projects_id_seq', 1, false);


--
-- TOC entry 3658 (class 0 OID 0)
-- Dependencies: 263
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tags_id_seq', 1, false);


--
-- TOC entry 3659 (class 0 OID 0)
-- Dependencies: 267
-- Name: tasks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tasks_id_seq', 1, false);


--
-- TOC entry 3660 (class 0 OID 0)
-- Dependencies: 269
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- TOC entry 3338 (class 2606 OID 45990)
-- Name: statistics statistics_pkey; Type: CONSTRAINT; Schema: common; Owner: postgres
--

ALTER TABLE ONLY common.statistics
    ADD CONSTRAINT statistics_pkey PRIMARY KEY (id);


--
-- TOC entry 3356 (class 2606 OID 46065)
-- Name: additional_equipment additional_equipment_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.additional_equipment
    ADD CONSTRAINT additional_equipment_pkey PRIMARY KEY (id);


--
-- TOC entry 3354 (class 2606 OID 46058)
-- Name: advantage advantage_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.advantage
    ADD CONSTRAINT advantage_pkey PRIMARY KEY (id);


--
-- TOC entry 3366 (class 2606 OID 46106)
-- Name: block block_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.block
    ADD CONSTRAINT block_pkey PRIMARY KEY (id);


--
-- TOC entry 3350 (class 2606 OID 46038)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 3368 (class 2606 OID 46113)
-- Name: doc doc_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.doc
    ADD CONSTRAINT doc_pkey PRIMARY KEY (id);


--
-- TOC entry 3352 (class 2606 OID 46049)
-- Name: equipment equipment_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.equipment
    ADD CONSTRAINT equipment_pkey PRIMARY KEY (id);


--
-- TOC entry 3358 (class 2606 OID 46072)
-- Name: main_equipment main_equipment_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.main_equipment
    ADD CONSTRAINT main_equipment_pkey PRIMARY KEY (id);


--
-- TOC entry 3372 (class 2606 OID 46127)
-- Name: sertificate_category sertificate_category_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate_category
    ADD CONSTRAINT sertificate_category_pkey PRIMARY KEY (id);


--
-- TOC entry 3370 (class 2606 OID 46120)
-- Name: sertificate sertificate_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate
    ADD CONSTRAINT sertificate_pkey PRIMARY KEY (id);


--
-- TOC entry 3364 (class 2606 OID 46099)
-- Name: size size_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.size
    ADD CONSTRAINT size_pkey PRIMARY KEY (id);


--
-- TOC entry 3360 (class 2606 OID 46081)
-- Name: subcategory subcategory_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.subcategory
    ADD CONSTRAINT subcategory_pkey PRIMARY KEY (id);


--
-- TOC entry 3362 (class 2606 OID 46090)
-- Name: type type_pkey; Type: CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.type
    ADD CONSTRAINT type_pkey PRIMARY KEY (id);


--
-- TOC entry 3340 (class 2606 OID 45997)
-- Name: img img_pkey; Type: CONSTRAINT; Schema: img; Owner: postgres
--

ALTER TABLE ONLY img.img
    ADD CONSTRAINT img_pkey PRIMARY KEY (id);


--
-- TOC entry 3346 (class 2606 OID 46022)
-- Name: equipment_industry equipment_industry_pkey; Type: CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.equipment_industry
    ADD CONSTRAINT equipment_industry_pkey PRIMARY KEY (id);


--
-- TOC entry 3342 (class 2606 OID 46006)
-- Name: industry industry_pkey; Type: CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.industry
    ADD CONSTRAINT industry_pkey PRIMARY KEY (id);


--
-- TOC entry 3348 (class 2606 OID 46029)
-- Name: object_equipment_category object_equipment_category_pkey; Type: CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object_equipment_category
    ADD CONSTRAINT object_equipment_category_pkey PRIMARY KEY (id);


--
-- TOC entry 3344 (class 2606 OID 46015)
-- Name: object object_pkey; Type: CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object
    ADD CONSTRAINT object_pkey PRIMARY KEY (id);


--
-- TOC entry 3374 (class 2606 OID 75806)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3376 (class 2606 OID 75817)
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- TOC entry 3378 (class 2606 OID 75823)
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- TOC entry 3380 (class 2606 OID 75838)
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);


--
-- TOC entry 3382 (class 2606 OID 75844)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3395 (class 2606 OID 46188)
-- Name: additional_equipment additional_equipment_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.additional_equipment
    ADD CONSTRAINT additional_equipment_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3396 (class 2606 OID 46193)
-- Name: additional_equipment additional_equipment_main_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.additional_equipment
    ADD CONSTRAINT additional_equipment_main_equipment_id_fkey FOREIGN KEY (main_equipment_id) REFERENCES equipment.main_equipment(id);


--
-- TOC entry 3394 (class 2606 OID 46183)
-- Name: advantage advantage_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.advantage
    ADD CONSTRAINT advantage_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3401 (class 2606 OID 46218)
-- Name: block block_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.block
    ADD CONSTRAINT block_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3402 (class 2606 OID 46223)
-- Name: block block_size_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.block
    ADD CONSTRAINT block_size_id_fkey FOREIGN KEY (size_id) REFERENCES equipment.size(id);


--
-- TOC entry 3392 (class 2606 OID 46173)
-- Name: category category_img_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.category
    ADD CONSTRAINT category_img_id_fkey FOREIGN KEY (img_id) REFERENCES img.img(id);


--
-- TOC entry 3403 (class 2606 OID 46228)
-- Name: doc doc_block_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.doc
    ADD CONSTRAINT doc_block_id_fkey FOREIGN KEY (block_id) REFERENCES equipment.block(id);


--
-- TOC entry 3393 (class 2606 OID 46178)
-- Name: equipment equipment_type_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.equipment
    ADD CONSTRAINT equipment_type_id_fkey FOREIGN KEY (type_id) REFERENCES equipment.type(id);


--
-- TOC entry 3397 (class 2606 OID 46198)
-- Name: main_equipment main_equipment_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.main_equipment
    ADD CONSTRAINT main_equipment_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3405 (class 2606 OID 46243)
-- Name: sertificate_category sertificate_category_category_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate_category
    ADD CONSTRAINT sertificate_category_category_id_fkey FOREIGN KEY (category_id) REFERENCES equipment.category(id);


--
-- TOC entry 3406 (class 2606 OID 46238)
-- Name: sertificate_category sertificate_category_img_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate_category
    ADD CONSTRAINT sertificate_category_img_id_fkey FOREIGN KEY (img_id) REFERENCES img.img(id);


--
-- TOC entry 3404 (class 2606 OID 46233)
-- Name: sertificate sertificate_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.sertificate
    ADD CONSTRAINT sertificate_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3400 (class 2606 OID 46213)
-- Name: size size_equipment_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.size
    ADD CONSTRAINT size_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3398 (class 2606 OID 46203)
-- Name: subcategory subcategory_category_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.subcategory
    ADD CONSTRAINT subcategory_category_id_fkey FOREIGN KEY (category_id) REFERENCES equipment.category(id);


--
-- TOC entry 3399 (class 2606 OID 46208)
-- Name: type type_subcategory_id_fkey; Type: FK CONSTRAINT; Schema: equipment; Owner: postgres
--

ALTER TABLE ONLY equipment.type
    ADD CONSTRAINT type_subcategory_id_fkey FOREIGN KEY (subcategory_id) REFERENCES equipment.subcategory(id);


--
-- TOC entry 3383 (class 2606 OID 46133)
-- Name: img img_equipment_id_fkey; Type: FK CONSTRAINT; Schema: img; Owner: postgres
--

ALTER TABLE ONLY img.img
    ADD CONSTRAINT img_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3384 (class 2606 OID 46128)
-- Name: img img_object_id_fkey; Type: FK CONSTRAINT; Schema: img; Owner: postgres
--

ALTER TABLE ONLY img.img
    ADD CONSTRAINT img_object_id_fkey FOREIGN KEY (object_id) REFERENCES industry.object(id);


--
-- TOC entry 3385 (class 2606 OID 46138)
-- Name: img img_size_id_fkey; Type: FK CONSTRAINT; Schema: img; Owner: postgres
--

ALTER TABLE ONLY img.img
    ADD CONSTRAINT img_size_id_fkey FOREIGN KEY (size_id) REFERENCES equipment.size(id);


--
-- TOC entry 3388 (class 2606 OID 46158)
-- Name: equipment_industry equipment_industry_equipment_category_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.equipment_industry
    ADD CONSTRAINT equipment_industry_equipment_category_id_fkey FOREIGN KEY (equipment_category_id) REFERENCES equipment.category(id);


--
-- TOC entry 3389 (class 2606 OID 46153)
-- Name: equipment_industry equipment_industry_industry_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.equipment_industry
    ADD CONSTRAINT equipment_industry_industry_id_fkey FOREIGN KEY (industry_id) REFERENCES industry.industry(id);


--
-- TOC entry 3386 (class 2606 OID 46143)
-- Name: industry industry_img_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.industry
    ADD CONSTRAINT industry_img_id_fkey FOREIGN KEY (img_id) REFERENCES img.img(id);


--
-- TOC entry 3390 (class 2606 OID 46168)
-- Name: object_equipment_category object_equipment_category_equipment_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object_equipment_category
    ADD CONSTRAINT object_equipment_category_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES equipment.equipment(id);


--
-- TOC entry 3391 (class 2606 OID 46163)
-- Name: object_equipment_category object_equipment_category_object_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object_equipment_category
    ADD CONSTRAINT object_equipment_category_object_id_fkey FOREIGN KEY (object_id) REFERENCES industry.object(id);


--
-- TOC entry 3387 (class 2606 OID 46148)
-- Name: object object_industry_id_fkey; Type: FK CONSTRAINT; Schema: industry; Owner: postgres
--

ALTER TABLE ONLY industry.object
    ADD CONSTRAINT object_industry_id_fkey FOREIGN KEY (industry_id) REFERENCES industry.industry(id);


--
-- TOC entry 3414 (class 2606 OID 75880)
-- Name: task_users fk6cv479hyfnkedc7nqc46tmag6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_users
    ADD CONSTRAINT fk6cv479hyfnkedc7nqc46tmag6 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3412 (class 2606 OID 75875)
-- Name: task_tags fk7xi1reghkj37gqwlr1ujxrxll; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_tags
    ADD CONSTRAINT fk7xi1reghkj37gqwlr1ujxrxll FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3407 (class 2606 OID 75855)
-- Name: comments fk8omq0tc18jd43bu5tjh6jvraq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3410 (class 2606 OID 75860)
-- Name: project_users fk8w55cu3qmg4yo0vy0b8e3ivk4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_users
    ADD CONSTRAINT fk8w55cu3qmg4yo0vy0b8e3ivk4 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3415 (class 2606 OID 75885)
-- Name: task_users fka7gicndcly21nk29xxfiuu9o7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_users
    ADD CONSTRAINT fka7gicndcly21nk29xxfiuu9o7 FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3413 (class 2606 OID 75870)
-- Name: task_tags fkeiqe3k9ent7icelm1cihqn164; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_tags
    ADD CONSTRAINT fkeiqe3k9ent7icelm1cihqn164 FOREIGN KEY (tag_id) REFERENCES public.tags(id);


--
-- TOC entry 3408 (class 2606 OID 75845)
-- Name: comments fkgkoamotsfr3mc0pwa1qrrmwhi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fkgkoamotsfr3mc0pwa1qrrmwhi FOREIGN KEY (project_id) REFERENCES public.projects(id);


--
-- TOC entry 3409 (class 2606 OID 75850)
-- Name: comments fki7pp0331nbiwd2844kg78kfwb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fki7pp0331nbiwd2844kg78kfwb FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3411 (class 2606 OID 75865)
-- Name: project_users fkn2d9w5xxgord5j4k2963p8o1g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_users
    ADD CONSTRAINT fkn2d9w5xxgord5j4k2963p8o1g FOREIGN KEY (project_id) REFERENCES public.projects(id);


--
-- TOC entry 3416 (class 2606 OID 75890)
-- Name: tasks fksfhn82y57i3k9uxww1s007acc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fksfhn82y57i3k9uxww1s007acc FOREIGN KEY (project_id) REFERENCES public.projects(id);


-- Completed on 2025-12-27 08:28:46

--
-- PostgreSQL database dump complete
--

--
-- Database "rbpo" dump
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.0
-- Dumped by pg_dump version 15.2

-- Started on 2025-12-27 08:28:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3378 (class 1262 OID 76087)
-- Name: rbpo; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE rbpo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE rbpo OWNER TO postgres;

\connect rbpo

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 865 (class 1247 OID 76147)
-- Name: task_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.task_status AS ENUM (
    'OPEN',
    'IN_PROGRESS',
    'DONE'
);


ALTER TYPE public.task_status OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 76094)
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id bigint NOT NULL,
    text character varying(255) NOT NULL,
    task_id bigint NOT NULL,
    user_id bigint NOT NULL,
    project_id bigint
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 76131)
-- Name: project_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.project_users (
    project_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.project_users OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 76091)
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projects (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255)
);


ALTER TABLE public.projects OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 76100)
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.tags OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 76140)
-- Name: task_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_tags (
    task_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.task_tags OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 76357)
-- Name: task_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task_users (
    task_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.task_users OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 76097)
-- Name: tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    status character varying(255) NOT NULL,
    project_id bigint NOT NULL
);


ALTER TABLE public.tasks OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 76088)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3367 (class 0 OID 76094)
-- Dependencies: 216
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, text, task_id, user_id, project_id) FROM stdin;
2	Комментарий к задаче 1 проект 2	2	2	\N
3	Комментарий к задаче 1 проект 2	2	4	\N
1	Комментарий к задаче 1 ОТРЕДАКТИРОВАНО	1	1	\N
\.


--
-- TOC entry 3370 (class 0 OID 76131)
-- Dependencies: 219
-- Data for Name: project_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.project_users (project_id, user_id) FROM stdin;
1	1
1	3
2	2
2	4
\.


--
-- TOC entry 3366 (class 0 OID 76091)
-- Dependencies: 215
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.projects (id, name, description) FROM stdin;
1	Проект 1 	Описание проекта 1 
2	Проект 2	Описание проекта 2 
\.


--
-- TOC entry 3369 (class 0 OID 76100)
-- Dependencies: 218
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tags (id, name) FROM stdin;
1	tag 3
2	backend
\.


--
-- TOC entry 3371 (class 0 OID 76140)
-- Dependencies: 220
-- Data for Name: task_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_tags (task_id, tag_id) FROM stdin;
1	1
\.


--
-- TOC entry 3372 (class 0 OID 76357)
-- Dependencies: 221
-- Data for Name: task_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_users (task_id, user_id) FROM stdin;
1	1
2	2
2	4
3	2
\.


--
-- TOC entry 3368 (class 0 OID 76097)
-- Dependencies: 217
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tasks (id, name, description, status, project_id) FROM stdin;
2	Задача 1	Описание задачи 1	IN_PROGRESS	2
1	Задача 1 ОТРЕДАКИРОВАНО	Описание задачи 1 ОТРЕДАКТИРОВАНО	OPEN	1
3	Задача 1	Описание задачи 1	IN_PROGRESS	2
\.


--
-- TOC entry 3365 (class 0 OID 76088)
-- Dependencies: 214
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name) FROM stdin;
2	Alice
3	Анастасия
4	Кристина
1	Человек
\.


--
-- TOC entry 3208 (class 2606 OID 76137)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3206 (class 2606 OID 76126)
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- TOC entry 3212 (class 2606 OID 76139)
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- TOC entry 3210 (class 2606 OID 76130)
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);


--
-- TOC entry 3204 (class 2606 OID 76337)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3221 (class 2606 OID 76360)
-- Name: task_users fk6cv479hyfnkedc7nqc46tmag6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_users
    ADD CONSTRAINT fk6cv479hyfnkedc7nqc46tmag6 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3219 (class 2606 OID 76202)
-- Name: task_tags fk7xi1reghkj37gqwlr1ujxrxll; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_tags
    ADD CONSTRAINT fk7xi1reghkj37gqwlr1ujxrxll FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3213 (class 2606 OID 76338)
-- Name: comments fk8omq0tc18jd43bu5tjh6jvraq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3217 (class 2606 OID 76343)
-- Name: project_users fk8w55cu3qmg4yo0vy0b8e3ivk4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_users
    ADD CONSTRAINT fk8w55cu3qmg4yo0vy0b8e3ivk4 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3222 (class 2606 OID 76365)
-- Name: task_users fka7gicndcly21nk29xxfiuu9o7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_users
    ADD CONSTRAINT fka7gicndcly21nk29xxfiuu9o7 FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3220 (class 2606 OID 76197)
-- Name: task_tags fkeiqe3k9ent7icelm1cihqn164; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task_tags
    ADD CONSTRAINT fkeiqe3k9ent7icelm1cihqn164 FOREIGN KEY (tag_id) REFERENCES public.tags(id);


--
-- TOC entry 3214 (class 2606 OID 76172)
-- Name: comments fkgkoamotsfr3mc0pwa1qrrmwhi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fkgkoamotsfr3mc0pwa1qrrmwhi FOREIGN KEY (project_id) REFERENCES public.projects(id);


--
-- TOC entry 3215 (class 2606 OID 76177)
-- Name: comments fki7pp0331nbiwd2844kg78kfwb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fki7pp0331nbiwd2844kg78kfwb FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- TOC entry 3218 (class 2606 OID 76192)
-- Name: project_users fkn2d9w5xxgord5j4k2963p8o1g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.project_users
    ADD CONSTRAINT fkn2d9w5xxgord5j4k2963p8o1g FOREIGN KEY (project_id) REFERENCES public.projects(id);


--
-- TOC entry 3216 (class 2606 OID 76217)
-- Name: tasks fksfhn82y57i3k9uxww1s007acc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fksfhn82y57i3k9uxww1s007acc FOREIGN KEY (project_id) REFERENCES public.projects(id);


-- Completed on 2025-12-27 08:28:46

--
-- PostgreSQL database dump complete
--

-- Completed on 2025-12-27 08:28:46

--
-- PostgreSQL database cluster dump complete
--

