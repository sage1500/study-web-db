

/* Create Sequences */

CREATE SEQUENCE IF NOT EXISTS order_id_seq;



/* Create Tables */

CREATE TABLE t_item
(
	code varchar(8) NOT NULL,
	name varchar(64) NOT NULL,
	PRIMARY KEY (code)
);


CREATE TABLE t_order
(
	id bigint DEFAULT nextval('order_id_seq') NOT NULL,
	status_code int NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE t_order_item
(
	order_id bigint NOT NULL,
	item_code varchar(8) NOT NULL
);



/* Create Foreign Keys */

ALTER TABLE t_order_item
	ADD FOREIGN KEY (item_code)
	REFERENCES t_item (code)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE t_order_item
	ADD FOREIGN KEY (order_id)
	REFERENCES t_order (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



