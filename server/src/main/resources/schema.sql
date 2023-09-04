-- CREATE DATABASE shophouse;

USE shophouse;

-- CREATE TABLE business(
-- 	   business_id		int	auto_increment 	not null,
--     business_name	varchar(128)		not null,
--     address			varchar(258)		not null,
--     phone			int(8),
--     email			varchar(128),
--     website			varchar(128),
-- 	   logo 			varchar(128),
--     constraint		pk_business_id		primary key(business_id)
-- );

-- CREATE TABLE category(
-- 	category_id		int auto_increment	not null,
--     category		varchar(128)		not null,
--     constraint		pk_category_id		primary key(category_id)
-- );

CREATE TABLE business_category(
	business_id		int		not null,
    category_id		int		not null,
    constraint		fk_business_id		foreign key(business_id)	references	business(business_id),
    constraint		fk_category_id		foreign key(category_id)	references	category(category_id)
)