-- Crear la tabla emails
CREATE TABLE emails (
                        email_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  -- ID del correo
                        email_from VARCHAR(255) NOT NULL,                              -- Correo del remitente
                        email_body TEXT NOT NULL,                                      -- Cuerpo del correo
                        state VARCHAR(50),
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                -- Fecha de actualización
                        email_to TEXT[],                                               -- Lista de destinatarios "To" (Array de strings)
                        email_cc TEXT[]                                                -- Lista de destinatarios "CC" (Array de strings)
);