--liquibase formatted sql
--changeset order:03-order-seed runAlways:true context:local,dev onError:MARK_RAN
-- Os dados de pedidos são apenas para ambiente de desenvolvimento

-- Pedido 1
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT 'asdas2332', 'ORD-2025-00000001', 'COMPLETED', 32.80, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000001'
);

-- Itens do pedido 1
-- X-Burger
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 1, 1, 'X-Burger', 1, 22.90, 'Sem cebola', now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 1 AND product_id = 1
);

-- Refrigerante Lata
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 1, 12, 'Refrigerante Lata', 1, 6.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 1 AND product_id = 12
);

-- Pedido 2
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT 'asd34515232', 'ORD-2025-00000002', 'READY', 79.70, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000002'
);

-- X-Salada
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 2, 3, 'X-Salada', 2, 20.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 2 AND product_id = 3
);

-- X-Bacon
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 2, 2, 'X-Bacon', 1, 24.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 2 AND product_id = 2
);

-- Refrigerante Lata
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 2, 12, 'Refrigerante Lata', 2, 6.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 2 AND product_id = 12
);

-- Pedido 3
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT 'asd34515232', 'ORD-2025-00000003', 'RECEIVED', 19.90, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000003'
);

-- Água Mineral
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 3, 15, 'Água Mineral', 1, 4.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 3 AND product_id = 15
);

-- Batata Frita G
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 3, 7, 'Batata Frita G', 1, 15.00, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 3 AND product_id = 7
);

-- Pedido 4
INSERT INTO orders (user_id, order_number, status, amount, created_at, updated_at)
SELECT 'asd34515232', 'ORD-2025-00000004', 'RECEIVED', 4.90, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM orders WHERE order_number = 'ORD-2025-00000004'
);

-- Água Mineral (único item)
INSERT INTO order_items (order_id, product_id, name, quantity, unit_price, observations, created_at, updated_at)
SELECT 4, 15, 'Água Mineral', 1, 4.90, null, now(), now()
WHERE NOT EXISTS (
    SELECT 1 FROM order_items WHERE order_id = 4 AND product_id = 15
);
