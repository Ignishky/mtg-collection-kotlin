ALTER TABLE cards
    ADD COLUMN scryfall_eur      numeric not null default 0,
    ADD COLUMN scryfall_eur_foil numeric not null default 0,
    ADD COLUMN scryfall_usd      numeric not null default 0,
    ADD COLUMN scryfall_usd_foil numeric not null default 0;

ALTER TABLE events
    ALTER COLUMN payload TYPE varchar;
