package cc.lasmgratel.lasmutil.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.lasmgratel.lasmutil.UtilManager.getLogger;

public interface StringIndicator {
    static String substringIn(String str, String start, String end) {
        if (str.contains(start))
            return str.substring(str.indexOf(start) + start.length(), str.indexOf(end, str.indexOf(start) + start.length()));
        getLogger().accept("Sorry, but we cannot found string " + start + " in " + str);
        return "";
    }

    static String regexString(String str, String regex, int group) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }
}
