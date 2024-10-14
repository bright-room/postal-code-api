create table address(
    postal_code varchar(7) not null primary key,
    national_local_government_code varchar(6) not null,
    state_name_kanji varchar(20) not null,
    state_name_kana varchar(50) not null,
    state_name_latin varchar(50) not null,
    city_name_kanji varchar(20) not null,
    city_name_kana varchar(50) not null,
    city_name_latin varchar(50) not null,
    street_name_kanji varchar(20) not null,
    street_name_kana varchar(50) not null,
    street_name_latin varchar(50) not null
);
