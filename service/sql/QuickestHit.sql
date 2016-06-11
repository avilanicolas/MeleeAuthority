-- Find quickest hitbox
SELECT fullName AS 'characters', SA.description AS move, MIN(frame) AS earliestAttackFrame
FROM FrameStrips F
   JOIN Characters C ON C.id = charId
   JOIN SharedAnimations SA ON SA.internalName = animation
WHERE hitbox = true
GROUP BY charId, animation
ORDER BY MIN(frame) ASC
LIMIT 20;
