-- Find quickest hitbox
SELECT fullName AS 'character', SA.description AS move, MIN(frame) AS earliestAttackFrame
FROM FrameStrips F
   JOIN Characters C ON C.id = charId
   JOIN SharedAnimations SA ON SA.internalName = animation
WHERE hitbox = true
    AND (SA.internalName = 'AttackLw4'
        OR SA.internalName = 'AttackS4S'
        OR SA.internalName = 'AttackS4Hi'
        OR SA.internalName = 'AttackS4Lw'
        OR SA.internalName = 'AttackHi4')
GROUP BY charId, animation
ORDER BY MIN(frame) ASC
LIMIT 20;
