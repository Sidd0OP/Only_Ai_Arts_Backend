ALTER TABLE IF EXISTS public.oauth
    ADD COLUMN "time" timestamp with time zone NOT NULL DEFAULT now();