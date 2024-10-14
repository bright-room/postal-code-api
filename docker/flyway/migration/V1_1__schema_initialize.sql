create table prefecture(
    id serial not null primary key,
    code varchar(2) not null,
    name_kanji varchar(10) not null,
    name_kana varchar(30) not null,
    name_latin varchar(30) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table prefecture is '都道府県';
comment on column prefecture.id is 'ID';
comment on column prefecture.code is 'コード';
comment on column prefecture.name_kanji is '都道府県名(漢字)';
comment on column prefecture.name_kana is '都道府県名(カナ)';
comment on column prefecture.name_latin is '都道府県名(ローマ字)';
comment on column prefecture.created_at is '作成日時';

create table municipalities(
    id serial not null primary key,
    code varchar(3) not null,
    name_kanji varchar(10) not null,
    name_kana varchar(30) not null,
    name_latin varchar(30) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table municipalities is '市町村';
comment on column municipalities.id is 'ID';
comment on column municipalities.code is 'コード';
comment on column municipalities.name_kanji is '市町村名(漢字)';
comment on column municipalities.name_kana is '市町村名(カナ)';
comment on column municipalities.name_latin is '市町村名(ローマ字)';
comment on column municipalities.created_at is '作成日時';

create table national_local_government_identify(
    id serial not null primary key,
    prefecture_id integer not null,
    municipalities_id integer not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (prefecture_id) references prefecture(id),
    foreign key (municipalities_id) references municipalities(id),
    unique (prefecture_id, municipalities_id)
);
comment on table national_local_government_identify is '全国地方公共団体を表す識別子';
comment on column national_local_government_identify.id is 'ID';
comment on column national_local_government_identify.prefecture_id is '都道府県ID';
comment on column national_local_government_identify.municipalities_id is '市町村ID';
comment on column national_local_government_identify.created_at is '作成日時';

create table postal_code(
    id serial not null primary key,
    code varchar(7) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table postal_code is '郵便番号';
comment on column postal_code.id is 'ID';
comment on column postal_code.code is '番号';
comment on column postal_code.created_at is '作成日時';

create table address(
    id serial not null primary key,
    postal_code_id integer not null,
    national_local_government_identify_id integer not null,
    street_name_kanji varchar(20) not null,
    street_name_kana varchar(50) not null,
    street_name_latin varchar(50) not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (postal_code_id) references postal_code(id),
    foreign key (national_local_government_identify_id) references national_local_government_identify(id)
);
comment on table address is '住所';
comment on column address.id is 'ID';
comment on column address.postal_code_id is '郵便番号ID';
comment on column address.national_local_government_identify_id is '全国地方公共団体を表す識別子ID';
comment on column address.street_name_kanji is '地方名(漢字)';
comment on column address.street_name_kana is '地方名(カナ)';
comment on column address.street_name_latin is '地方名(ローマ字)';
comment on column address.created_at is '作成日時';

create table invalid_address(
    address_id integer not null primary key,
    reason varchar(100) not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (address_id) references address(id)
);
comment on table invalid_address is '無効な住所';
comment on column invalid_address.address_id is '住所ID';
comment on column invalid_address.reason is '理由';
comment on column invalid_address.created_at is '作成日時';

create materialized view address_view as
select address.*
from address
left join invalid_address
       on address.id = invalid_address.address_id
where invalid_address.address_id is null;
