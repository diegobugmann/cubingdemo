create table scramble (
                          created_at timestamp(6) not null,
                          id bigint auto_increment,
                          length bigint,
                          time bigint,
                          moves_as_string varchar(255),
                          primary key (id)
);