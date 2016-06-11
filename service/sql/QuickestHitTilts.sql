-- Find quickest hitbox
SELECT fullName AS 'character', SA.description AS move, MIN(frame) AS earliestAttackFrame
FROM FrameStrips F
   JOIN Characters C ON C.id = charId
   JOIN SharedAnimations SA ON SA.internalName = animation
WHERE hitbox = true
    AND (SA.internalName = 'AttackLw3'
        OR SA.internalName = 'AttackHi3'
        OR SA.internalName = 'AttackS3S'
        OR SA.internalName = 'AttackS3Hi'
        OR SA.internalName = 'AttackS3Lw')
GROUP BY charId, animation
ORDER BY MIN(frame) ASC
LIMIT 20;
