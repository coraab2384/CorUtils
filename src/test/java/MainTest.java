import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.cb2384.corutils.NullnessUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {
    /*@Test
    public void AnABoolTest() {
        String[] testStrings = new String[]{
                "Apple", "Yldl", "ilieugfrbelrikugbeq", "5356wfgwerh13r4f", "igloo",
                "Être", "Äpfel", "ẞit", "Observe", ""
        };
        boolean[] expected = new boolean[]{
                true, false, true, false, true,
                false, false, false, true, false
        };
        
        boolean[] observed = new boolean[testStrings.length];
        for (int i = 0; i < testStrings.length; i++) {
            observed[i] = StringUtils.startsWithVowel(testStrings[i]);
        }
        
        Assertions.assertArrayEquals(expected, observed);
    }
    
    @Test
    public void genericBoolTest() {
        String[] testStrings = new String[]{
                "Apple", "Yldl", "ilieugfrbelrikugbeq", "5356wfgwerh13r4f", "igloo",
                "Être", "Äpfel", "ßit", "Observe", ""
        };
        String[] firstValChecks = new String[]{
                "It", "yummy", "retgwbkhuj", "1234567890", "I",
                "ÂÊÎÔÛ", "ÄÖÜẞ", "äöüß", "\r\t\n\\", "qwertz"
        };
        boolean[] expected = new boolean[]{
                false, false, false, true, false,
                true, true, true, false, false
        };
        
        boolean[] observed = new boolean[testStrings.length];
        for (int i = 0; i < testStrings.length; i++) {
            observed[i] = StringUtils.firstCharIsOneOf(testStrings[i], firstValChecks[i]);
        }
        
        Assertions.assertArrayEquals(expected, observed);
    }
    
    @Test
    public void prependTest() {
        String[] tests = new String[] {
                "apple", "ugly duckling", " ant ",  "ẞilly", "car",
                " Umlaut", "bicycle", " leg ", "15364", ""
        };
        
        String[] expected = new String[] {
                "an apple", "an ugly duckling", "an ant ", "a ẞilly", "a car",
                " an Umlaut", " a bicycle", " a leg ", " a 15364", " a"
        };
        
        String[] observed = new String[10];
        for (int i = 0; i < 10; i++) {
            observed[i] = StringUtils.aOrAnPrepend(tests[i], i >= 5);
        }
        
        Assertions.assertArrayEquals(expected, observed);
    }*/
    
    @Test
    public void testDefaults() {
        Assertions.assertFalse(NullnessUtils.nullToFalse(false));
        Assertions.assertFalse(NullnessUtils.nullToFalse(null));
        Assertions.assertTrue(NullnessUtils.nullToFalse(Boolean.TRUE));
        
        Assertions.assertTrue(NullnessUtils.nullToTrue(true));
        Assertions.assertTrue(NullnessUtils.nullToTrue(null));
        Assertions.assertFalse(NullnessUtils.nullToTrue(Boolean.FALSE));
        
        List<BigInteger> bigInts = new ArrayList<>(Arrays.asList(forTest));
        
        Assertions.assertEquals(new Point(4, 5),
                NullnessUtils.returnDefaultIfNull(null, new Point(4, 5)));
        Assertions.assertEquals(Integer.TYPE,
                NullnessUtils.returnDefaultIfNull(Integer.TYPE, Long.TYPE));
        
        Assertions.assertEquals(BigInteger.ZERO,
                NullnessUtils.returnDefaultIfNull(bigInts, list -> list.get(0), BigInteger.ZERO));
        String legallyNotChina = Locale.TAIWAN.getCountry();
        Assertions.assertEquals(legallyNotChina,
                NullnessUtils.returnDefaultIfNull(null, Locale::getCountry, legallyNotChina));
        
        Assertions.assertEquals(BigInteger.ZERO,
                NullnessUtils.generateDefaultIfNull(null, () -> bigInts.get(0)));
        Assertions.assertEquals(legallyNotChina,
                NullnessUtils.generateDefaultIfNull(Locale.TAIWAN, Locale::getDefault).getCountry());
        
        StringBuilder testSB = new StringBuilder("test");
        Assertions.assertEquals(testSB.toString(),
                NullnessUtils.generateDefaultIfNull(testSB, StringBuilder::toString, System.out::toString));
        Assertions.assertEquals(BigInteger.valueOf(5), NullnessUtils.generateDefaultIfNull(
                null,
                BigInteger::valueOf,
                () -> bigInts.get(bigInts.size() - 1)
        ));
        
        Set<BigInteger> testSet = Arrays.stream(
                        new BigInteger[]{BigInteger.ONE, BigInteger.ZERO, BigInteger.TEN})
                .collect(Collectors.toCollection(HashSet::new));
        NullnessUtils.applyIfNonNull(BigInteger.valueOf(2), testSet::add);
        Assertions.assertEquals(4, testSet.size());
        NullnessUtils.applyIfNonNull(null, (Consumer<? super BigInteger>) x -> bigInts.add(0, x));
        Assertions.assertEquals(forTest.length, bigInts.size());
        
        Assertions.assertSame(null,
                NullnessUtils.applyIfNonNull(null, testSet::add));
        Assertions.assertEquals(false,
                NullnessUtils.applyIfNonNull(BigInteger.ONE, testSet::add));
    }
    
    private static final BigInteger[] forTest = new BigInteger[]{
            BigInteger.ZERO, BigInteger.ONE, BigInteger.valueOf(2),
            BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5)
    };
    
    @Test
    public void testArrayOps() {
        BigInteger[] forNullTest = Arrays.copyOf(forTest, forTest.length + 1);
        Assertions.assertFalse(NullnessUtils.arrayHasNulls(forTest));
        Assertions.assertThrowsExactly(
                NullPointerException.class,
                () -> NullnessUtils.checkNullArray(forNullTest)
        );
        BigInteger[][][] forDeepTest = new BigInteger[][][]{
                new BigInteger[][]{forTest, forTest},
                new BigInteger[][]{forTest, forTest, forTest},
                new BigInteger[0][]
        };
        Assertions.assertFalse(NullnessUtils.arrayHasDeepNulls(forDeepTest));
        forDeepTest[2] = new BigInteger[][]{forTest, forNullTest, null};
        Assertions.assertThrowsExactly(
                NullPointerException.class,
                () -> NullnessUtils.checkDeepNullArray(forDeepTest)
        );
    }
    /*
    @Test
    public void testIterAndCollec() {
        List<BigInteger> shallowList = new ArrayList<>(Arrays.asList(forTest));
        Set<BigInteger> shallowSet = new HashSet<>(shallowList);
        
        shallowList.addLast(null);
        Assertions.assertTrue(NullnessUtils.iterableHasNulls(shallowList, null));
        Assertions.assertFalse(NullnessUtils.collectionHasNulls(shallowSet));
        Assertions.assertThrowsExactly(
                NullPointerException.class,
                () -> NullnessUtils.checkNullCollec(shallowList)
        );
        shallowList.removeLast();
        
        List<?> deepList = Arrays.asList(
                new ArrayList<String>(), new ArrayList<Locale>(),
                new ArrayList<>(), new HashSet<List<?>>()
        );
        ((List<String>) deepList.get(0)).addAll(Arrays.asList(
                StringUtils.DEFAULT_VOWELS.substring(0, 2), StringUtils.DEFAULT_VOWELS.substring(2, 4),
                StringUtils.DEFAULT_VOWELS.substring(4, 6), StringUtils.DEFAULT_VOWELS.substring(6, 8),
                StringUtils.DEFAULT_VOWELS.substring(8), StringUtils.DEFAULT_VOWELS
        ));
        ((List<Locale>) deepList.get(1)).addAll(Arrays.asList(Locale.getAvailableLocales()));
        ((List<?>) deepList.get(2)).addAll((Collection<?>) Arrays.asList(
                new int[]{2, 3, 4, 1, 8, -9, 0x5e}, BigInteger.ONE
        ));
    }*/
}