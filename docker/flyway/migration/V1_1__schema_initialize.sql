create table migration_test(
    id serial not null primary key,
    created_at timestamp without time zone not null default current_timestamp
);
comment on table migration_test is 'マイグレーションテスト用';
comment on column migration_test.id is 'ID';
comment on column migration_test.created_at is '作成日時';
