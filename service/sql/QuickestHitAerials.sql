-- Find quickest hitbox
SELECT fullName AS 'character', SA.description AS move, MIN(frame) AS earliestAttackFrame
FROM FrameStrips F
   JOIN Characters C ON C.id = charId
   JOIN SharedAnimations SA ON SA.internalName = animation
WHERE hitbox = true
    AND (SA.internalName = 'AttackAirF'
        OR SA.internalName = 'AttackAirHi'
        OR SA.internalName = 'AttackAirLw'
        OR SA.internalName = 'AttackAirB'
        OR SA.internalName = 'AttackAirN')
GROUP BY charId, animation
ORDER BY MIN(frame) ASC
LIMIT 20;

-- quickest hitbox per character
SELECT C.fullName AS 'character', SA.description AS move, SS1.earliestFrame
FROM (
        SELECT charId, animation, MIN(frame) AS earliestFrame
        FROM FrameStrips
        WHERE
            hitbox = TRUE
            AND (animation = 'AttackAirF'
                OR animation = 'AttackAirHi'
                OR animation = 'AttackAirLw'
                OR animation = 'AttackAirB'
                OR animation = 'AttackAirN')
        GROUP BY charId, animation
    ) SS1 LEFT JOIN (
        -- loser aerials per character
        SELECT S1.charId, S1.animation
        FROM (
                SELECT charId, animation, MIN(frame) AS earliestFrame
                FROM FrameStrips
                WHERE
                    hitbox = TRUE
                    AND (animation = 'AttackAirF'
                        OR animation = 'AttackAirHi'
                        OR animation = 'AttackAirLw'
                        OR animation = 'AttackAirB'
                        OR animation = 'AttackAirN')
                GROUP BY charId, animation
            ) S1 JOIN (
                SELECT charId, animation, MIN(frame) AS earliestFrame
                FROM FrameStrips
                WHERE
                    hitbox = TRUE
                    AND (animation = 'AttackAirF'
                        OR animation = 'AttackAirHi'
                        OR animation = 'AttackAirLw'
                        OR animation = 'AttackAirB'
                        OR animation = 'AttackAirN')
                GROUP BY charId, animation
            ) S2 ON S1.charId = S2.charId AND S1.earliestFrame > S2.earliestFrame
    ) SS2 ON SS1.animation = SS2.animation AND SS1.charId = SS2.charId
    JOIN Characters C ON C.id = SS1.charId
    JOIN SharedAnimations SA ON SA.internalName = SS1.animation
WHERE SS2.animation IS NULL
ORDER BY earliestFrame, 'character';
