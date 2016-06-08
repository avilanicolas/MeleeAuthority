package main.java.meleeauthority;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableList;

public class MeleeMoveController {

    private static MeleeDB meleeDB = null;
    private static List<String> characterNames = null;
    private static List<String> characterIds = null;

    private static final String ALL = "ALL";
    private static final String NONE = "NONE";
    private static final float ZERO = 0;

    @RequestMapping("/move")
    public List<MeleeMove> move(
            @RequestParam(value="charId", defaultValue=ALL) String charId) {
        List<MeleeMove> list = ImmutableList.of();

        if (ALL.equals(charId)) {
            list = meleeDB.getAllMoves();
        } else if (validId(charId)) {
            
        }

       return list;
    }

    private boolean validId(String charId) {
        return getCharacterIds().contains(charId);
    }

    private boolean validName(String name) {
        return getCharacterNames().contains(name);        
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
