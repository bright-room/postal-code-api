create table prefectures(
    code varchar(2) not null primary key,
    name_kanji varchar(10) not null,
    name_kana varchar(30) not null,
    name_latin varchar(30) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table prefectures is '都道府県';
comment on column prefectures.code is 'コード';
comment on column prefectures.name_kanji is '都道府県名(漢字)';
comment on column prefectures.name_kana is '都道府県名(カナ)';
comment on column prefectures.name_latin is '都道府県名(ローマ字)';
comment on column prefectures.created_at is '作成日時';

create table municipalities(
    code varchar(3) not null primary key,
    name_kanji varchar(10) not null,
    name_kana varchar(30) not null,
    name_latin varchar(30) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table municipalities is '市町村';
comment on column municipalities.code is 'コード';
comment on column municipalities.name_kanji is '市町村名(漢字)';
comment on column municipalities.name_kana is '市町村名(カナ)';
comment on column municipalities.name_latin is '市町村名(ローマ字)';
comment on column municipalities.created_at is '作成日時';

create table national_local_government_identifies(
    id serial not null primary key,
    prefectures_code varchar(2) not null,
    municipalities_code varchar(3) not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (prefectures_code) references prefectures(code),
    foreign key (municipalities_code) references municipalities(code),
    unique (prefectures_code, municipalities_code)
);
comment on table national_local_government_identifies is '全国地方公共団体を表す識別子';
comment on column national_local_government_identifies.id is 'ID';
comment on column national_local_government_identifies.prefectures_code is '都道府県コード';
comment on column national_local_government_identifies.municipalities_code is '市町村コード';
comment on column national_local_government_identifies.created_at is '作成日時';

create table areas(
    id serial not null primary key,
    name_kanji varchar(10) not null,
    name_kana varchar(30) not null,
    name_latin varchar(30) not null,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table areas is '地域';
comment on column areas.id is 'ID';
comment on column areas.name_kanji is '地域名(漢字)';
comment on column areas.name_kana is '地域名(カナ)';
comment on column areas.name_latin is '地域名(ローマ字)';
comment on column areas.created_at is '作成日時';

create table postal_codes(
    code varchar(7) not null primary key
);
comment on table postal_codes is '郵便番号';
comment on column postal_codes.code is '番号';

create table address(
    id serial not null primary key,
    postal_code varchar(7) not null,
    national_local_government_identifies_id integer not null,
    areas_id integer not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (postal_code) references postal_codes(code),
    foreign key (national_local_government_identifies_id) references national_local_government_identifies(id),
    foreign key (areas_id) references areas(id)
);
comment on table address is '住所';
comment on column address.id is 'ID';
comment on column address.postal_code is '郵便番号';
comment on column address.national_local_government_identifies_id is '全国地方公共団体を表す識別子ID';
comment on column address.areas_id is '地域ID';
comment on column address.created_at is '作成日時';

create table invalid_address_reason(
    reason varchar(100) not null primary key
);

insert into invalid_address_reason(reason)
values ('市政_区政_町政_分区_政令指定都市施行'),
       ('住居表示の実施'),
       ('区画整理'),
       ('郵便区調整等'),
       ('訂正'),
       ('廃止');

create table invalid_address(
    address_id integer not null primary key,
    reason varchar(100) not null,
    created_at timestamp without time zone not null default current_timestamp,

    foreign key (address_id) references address(id),
    foreign key (reason) references invalid_address_reason(reason)
);
comment on table invalid_address is '無効な住所';
comment on column invalid_address.address_id is '住所ID';
comment on column invalid_address.reason is '理由';
comment on column invalid_address.created_at is '作成日時';
