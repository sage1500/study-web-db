/* Create Sequences */

CREATE SEQUENCE order_id_seq;



/* Create Tables */

CREATE TABLE t_item
(
	code varchar(8) NOT NULL,
	name varchar(64) NOT NULL,
	PRIMARY KEY (code)
) WITHOUT OIDS;


CREATE TABLE t_order
(
	id bigint DEFAULT nextval('order_id_seq') NOT NULL,
	status_code int NOT NULL,
	PRIMARY KEY (id)
) WITHOUT OIDS;


CREATE TABLE t_order_item
(
	order_id bigint NOT NULL,
	item_code varchar(8) NOT NULL
) WITHOUT OIDS;



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



/* Comments */

COMMENT ON TABLE t_item IS '品目';
COMMENT ON COLUMN t_item.code IS '品目コード';
COMMENT ON COLUMN t_item.name IS '品目名';
COMMENT ON TABLE t_order IS 'オーダー';
COMMENT ON COLUMN t_order.id IS 'オーダーID';
COMMENT ON COLUMN t_order.status_code IS 'ステータスコード';
COMMENT ON TABLE t_order_item IS 'オーダー品目';
COMMENT ON COLUMN t_order_item.order_id IS 'オーダーID';
COMMENT ON COLUMN t_order_item.item_code IS '品目コード';



