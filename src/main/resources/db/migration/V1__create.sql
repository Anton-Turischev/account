CREATE TABLE public.account
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    balance numeric(19,2),
    CONSTRAINT account_pkey PRIMARY KEY (id)
);

ALTER TABLE public.account
    OWNER to postgres;