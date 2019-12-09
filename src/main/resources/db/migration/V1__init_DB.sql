
 create sequence bid_sequence start 1 increment 1;
 create sequence product_sequence start 1 increment 1;
 create sequence review_sequence start 1 increment 1;
 create sequence users_sequence start 1 increment 1;

 create table bid ( primary key (id),
         id int8 not null,
         created_at timestamp,
         created_by varchar(20),
         updated_at timestamp,
         updated_by varchar(20),
         price numeric(19, 2),
         time timestamp,
         bidder_id int8 not null,
         product_id int8 not null);

 create table product (primary key (id),
         id int8 not null,
         created_at timestamp,
         created_by varchar(20),
         updated_at timestamp,
         updated_by varchar(20),
         description varchar(123) not null,
         expire_time date, image varchar(255),
         name varchar(255) not null,
         price numeric(19, 2),
         rating float8,
         user_id int8);

 create table review (primary key (id),
        id int8 not null,
        created_at timestamp,
        created_by varchar(20),
        updated_at timestamp,
        updated_by varchar(20),
        comment varchar(255),
        rating float8,
        time timestamp,
        product_id int8 not null,
        reviewer_id int8 not null);

 create table role (user_id int8 not null,
        user_roles varchar(255));

 create table users (primary key (id),
       id int8 not null,
       created_at timestamp,
       created_by varchar(20),
       updated_at timestamp,
       updated_by varchar(20),
       email varchar(255) not null,
       password varchar(255) not null,
       username varchar(255) not null);

 alter table product add constraint UK_product_name unique (name);
 alter table users add constraint UK_users_email unique (email);
 alter table users add constraint UK_users_username unique (username) ;

 alter table bid add constraint FK_bid_user foreign key (bidder_id) references users ;
 alter table bid add constraint FK_bid_product foreign key (product_id) references product ;
 alter table product add constraint FK_product_users foreign key (user_id) references users ;
 alter table review add constraint FK_review_product foreign key (product_id) references product ;
 alter table review add constraint FK_review_users foreign key (reviewer_id) references users ;
 alter table role add constraint FK_role_users foreign key (user_id) references users ;




