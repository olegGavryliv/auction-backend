insert into users (id, username, password, email)
 values  (nextval('users_sequence'), 'Tom', '$2a$10$IElANUperbJBfGln.UnW7eJe6en.GLUVEIcZS3Y15OChNAUyD9v/m', 'admin@gmail.com');

insert into role (user_id, user_roles)
 values (currval('users_sequence'), 'ROLE_ADMIN');

insert into users (id, username, password, email)
 values  (nextval('users_sequence'), 'user', '$2a$10$IElANUperbJBfGln.UnW7eJe6en.GLUVEIcZS3Y15OChNAUyD9v/m', 'user@gmail.com');

insert into role (user_id, user_roles)
 values   (currval('users_sequence'), 'ROLE_CLIENT');


insert into product (id, name, description, user_id, image )
 values (nextval('product_sequence'), 'Box1', 'My Box1', currval('users_sequence')-1, 'https://picsum.photos/900/500?random&amp;t=23423423' ),
       (nextval('product_sequence'), 'Box2', 'My Box2', currval('users_sequence'), 'https://picsum.photos/900/500?random&amp;t=234223' ),
       (nextval('product_sequence'), 'Box3', 'My Box3', currval('users_sequence'),  'https://picsum.photos/900/500?random&amp;t=234223' ),
       (nextval('product_sequence'), 'Box4', 'My Box4', currval('users_sequence')-1, 'https://picsum.photos/900/500?random&amp;t=33242' ),
       (nextval('product_sequence'), 'Box5', 'My Box5', currval('users_sequence'),  'https://picsum.photos/900/500?random&amp;t=423414' ),
       (nextval('product_sequence'), 'Box6', 'My Box6', currval('users_sequence')-1, 'https://picsum.photos/900/500?random&amp;t=21414124' );

insert into bid (id, price, time , bidder_id, product_id)
 values (nextval('bid_sequence'), 4000, now(), currval('users_sequence'), currval('product_sequence')-2 ),
       (nextval('bid_sequence'), 3000, now(), currval('users_sequence'), currval('product_sequence')-1 );


insert into review (id, rating, comment, time, reviewer_id, product_id )
 values (nextval('review_sequence'), 5, 'Very good quality', now(), currval('users_sequence')-1, currval('product_sequence')-1),
       (nextval('review_sequence'), 1, 'Very bad quality', now(), currval('users_sequence'), currval('product_sequence')-2),
       (nextval('review_sequence'), 4, 'Not bad quality', now(), currval('users_sequence'), currval('product_sequence')-3),
       (nextval('review_sequence'), 4, 'Not bad quality', now(), currval('users_sequence')-1, currval('product_sequence')-4),
       (nextval('review_sequence'), 4, 'Not bad quality', now(), currval('users_sequence'), currval('product_sequence')-1),
       (nextval('review_sequence'), 4, 'Not bad quality', now(), currval('users_sequence')-1, currval('product_sequence')-2);


