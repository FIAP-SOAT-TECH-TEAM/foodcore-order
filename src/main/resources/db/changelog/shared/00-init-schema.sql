--liquibase formatted sql

--changeset shared:00-init-schema context:local,dev,prod
-- Tipos enumerados (necessários em todos os ambientes)

-- Enum para status de pedido
--changeset order-types:create-order-status-enum runOnChange:true
CREATE TYPE order_status_enum AS ENUM ('RECEIVED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED');
