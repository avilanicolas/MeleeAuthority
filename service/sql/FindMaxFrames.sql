-- Order moves by most frames with hitboxes open
SELECT C.fullName, 
   SA.description, 
   COUNT(*) AS totalFrames,
   hitboxFrames,
   hitboxFrames * 1.0/COUNT(*) AS hitboxRatio
FROM FrameStrips F
   JOIN (
      SELECT charId, animation, COUNT(*) AS hitboxFrames
      FROM FrameStrips
      WHERE hitbox = true
      GROUP BY charId, animation
   ) HB ON HB.charId = F.charId AND HB.animation = F.animation
   JOIN Characters C ON C.id = F.charId
   JOIN SharedAnimations SA ON SA.internalName = F.animation
GROUP BY F.charId, F.animation
ORDER BY hitboxRatio DESC, C.fullName;

