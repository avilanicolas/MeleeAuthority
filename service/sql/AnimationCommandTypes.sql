CREATE TABLE AnimationCommandTypes (
    id INTEGER,
    name VARCHAR(32),
    numBytes INTEGER,
    PRIMARY KEY (id),
    UNIQUE (name)
);

INSERT INTO AnimationCommandTypes
    (id, name, numBytes)
VALUES
    (44, 'Hitbox', 20),
    (136, 'Throw', 12),
    (224, 'Start Smash Charge', 8),
    (104, 'Body State', 4),
    (204, 'Self-Damage', 4),
    (64, 'Terminate Collisions', 4),
    (208, 'Continuation control?', 4),
    (92, 'Allow Interrupt (IASA)', 4),
    (76, 'Autocancel', 4),
    (80, 'Reverse Direction?', 4),
    (4, 'Synchronous Timer', 4),
    (8, 'Asynchronous Timer', 4),
    (12, 'Set Loop', 4),
    (16, 'Execute Loop', 4),
    (28, 'Subroutine', 8),
    (20, 'GoTo', 8),
    (24, 'Return', 4),
    (172, 'Generate article?', 4),
    (124, 'Model mod', 4),
    (40, 'Graphic Effect', 20),
    (68, 'Sound Effect', 12),
    (72, 'Random Smash SFX', 4),
    (60, 'Terminate Specific Collision', 4),
    (108, 'Set All Bones State', 4),
    (112, 'Set Specific Bone State', 4),
    (232, 'TBD', 16),
    (116, 'Unknown', 4);
