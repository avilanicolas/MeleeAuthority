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
