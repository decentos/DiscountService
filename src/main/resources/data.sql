insert into discount_period(start_date, end_date, percent) values ('2020-08-01', '2020-08-10', 30);
insert into discount_period(start_date, end_date, percent) values ('2020-08-11', '2020-08-20', 50);
insert into discount_period(start_date, end_date, percent) values ('2020-08-21', '2020-09-10', 40);

insert into discount_percent(percent) values (10);
insert into discount_percent(percent) values (15);
insert into discount_percent(percent) values (20);

insert into discount_item(price, base_price, discount_percent_id, discount_period_id) values (900.00, 1000.00, 3, 1);
insert into discount_item(price, base_price, discount_percent_id, discount_period_id) values (3600.00, 4000.00, 2, 2);
insert into discount_item(price, base_price, discount_percent_id, discount_period_id) values (4500.00, 5000.00, 1, 3);

insert into discount_configure(sum_start, sum_end, percent) values (1.00, 5000.00, 3);
insert into discount_configure(sum_start, sum_end, percent) values (5001.00, 10000.00, 5);
insert into discount_configure(sum_start, sum_end, percent) values (10001.00, 100000.00, 10);
insert into discount_configure(sum_start, sum_end, percent) values (100001.00, 1000000000.00, 15);

insert into discount_user(user_id, purchase_amount, discount_configure_id) values (1, 3000.00, 1);
insert into discount_user(user_id, purchase_amount, discount_configure_id) values (2, 6000.00, 2);
insert into discount_user(user_id, purchase_amount, discount_configure_id) values (3, 25000.00, 3);