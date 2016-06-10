package main.java.meleeauthority;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@RestController
public class MeleeMoveController {

    private static MeleeDB meleeDB = null;
    private static List<String> characterNames = null;
    private static List<String> characterIds = null;
    private static List<String> characterAnimations = null;

    private static final String ALL = "ALL";
    private static final String NONE = "NONE";

    @RequestMapping("/hitbox")
    public Map<String, List<Hitbox>> hitbox(
            @RequestParam(value="charId", defaultValue=NONE) String charId,
            @RequestParam(value="animation", defaultValue=ALL) String animation) {

        if (NONE.equals(charId)) {
            return ImmutableMap.of();
        }

        if (!NONE.equals(charId) && ALL.equals(animation)) {
            if (validId(charId)) {
                return getDB().getHitboxesForCharacter(charId);
            }
        }

        if (validId(charId) && validAnimation(animation)) {
            return ImmutableMap.of(animation, getDB().getHitboxesForMove(charId, animation));
        }

        return ImmutableMap.of();
    }

    @RequestMapping("/move")
    public List<MeleeMove> move(
            @RequestParam(value="charId", defaultValue=ALL) String charId,
            @RequestParam(value="animation", defaultValue=NONE) String animation) {
        
        List<MeleeMove> list = ImmutableList.of();

        if (!NONE.equals(animation) && !ALL.equals(charId)) {
            if (validId(charId) && validAnimation(animation)) {
                return getDB().getMovesQualifiedBy(charId, animation);
            }
        } else if (!NONE.equals(animation)) {
            if (validAnimation(animation)) {
                return getDB().getMovesQualifiedBy(null, animation);
            }
        }

        if (ALL.equals(charId)) {
            return getDB().getAllMoves();
        } else if (validId(charId)) {
            return getDB().getMovesForCharacter(charId);
        }

       return list;
    }

    private boolean validId(String charId) {
        return getCharacterIds().contains(charId);
    }

    private boolean validAnimation(String animation) {
        return getCharacterAnimations().contains(animation);
    }

    private boolean validName(String name) {
        return getCharacterNames().contains(name);        
    }

    private List<String> getCharacterAnimations() {
        if (characterAnimations == null) {
            characterAnimations = getDB().getCharacterAnimations();
        }
        return characterAnimations;
    }

    private List<String> getCharacterIds() {
        if (characterIds == null) {
            characterIds = getDB().getCharacterIds();
        }

        return characterIds;
    }

    private List<String> getCharacterNames() {
        if (characterNames == null) {
            characterNames = getDB().getCharacterNames();
        }

        return characterNames;
    }

    private MeleeDB getDB() {
        if (meleeDB == null) {
            meleeDB = Application.getDB();
        }

        return meleeDB;
    }
}
