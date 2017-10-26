package com.gigamog.democitrine.service;

import com.gigamog.democitrine.domain.ConversionRepository;
import com.gigamog.democitrine.domain.model.Conversion;
import com.gigamog.democitrine.domain.model.Si;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SiService {
    private final ConversionRepository conversionRepository;


    private static final Comparator<Conversion> SORT_BY_LONGEST_NAME = (x, y) ->
            Integer.valueOf(y.getName().length()).compareTo(x.getName().length());
    private static final Comparator<Conversion> SORT_BY_LONGEST_SYMBOL = (x, y) ->
            Integer.valueOf(y.getSymbol().length()).compareTo(x.getSymbol().length());

    private static final Function<Conversion, String> useName = conversion -> (conversion.getName());
    private static final Function<Conversion, String> useSymbol = conversion -> (conversion.getSymbol());

    public SiService(ConversionRepository conversionRepository) {
        this.conversionRepository = conversionRepository;
    }

    public Si getSi(String input) {


        /**
         *  Originally the code was much cleaner but I ran into a bug where the wrong string was getting replaced.
         *  If I had more time I would like to write something more robust. But as of now this works.
         *
         */
        List<Conversion> conversionListByName = sortConversions(SORT_BY_LONGEST_NAME);
        List<Conversion> conversionListBySymbol = sortConversions(SORT_BY_LONGEST_SYMBOL);

        StringBuilder workingValueEquation = new StringBuilder(input);
        StringBuilder unitName = new StringBuilder(input);

        processList(conversionListByName, workingValueEquation, unitName, useName);
        processList(conversionListBySymbol, workingValueEquation, unitName, useSymbol);

        String multiplicationFactor = StringEquationToResult(workingValueEquation.toString());
        return tryToCreateSi(unitName.toString(), multiplicationFactor);
    }


    /**
     * if the user gives me an invalid input this will catch it and return empty
     *
     * @param unitName
     * @param multiplicationFactor
     * @return
     */
    private Si tryToCreateSi(String unitName, String multiplicationFactor) {
        try {
            return Si.builder()
                    .unitName(unitName)
                    .multiplicationFactor(new BigDecimal(multiplicationFactor))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Si.builder()
                    .unitName("")
                    .multiplicationFactor(null)
                    .build();
        }
    }


    /**
     * I grab the given input and change it two strings. The one that is going to turn into the multiplication factor and the one that shows the symbols
     *
     * @param conversionList
     * @param workingValueEquationBuilder
     * @param unitNameBuilder
     * @param function
     */
    private void processList(List<Conversion> conversionList,
                             StringBuilder workingValueEquationBuilder,
                             StringBuilder unitNameBuilder,
                             Function<Conversion, String> function) {
        String workingValueEquation = workingValueEquationBuilder.toString();
        String unitName = unitNameBuilder.toString();
        for (Conversion conversion : conversionList) {
            workingValueEquation = replaceByKeys(workingValueEquation,
                    String.valueOf(conversion.getWorkingValue()), function.apply(conversion));
            unitName = replaceByKeys(unitName, conversion.getSi(), function.apply(conversion));
        }
        workingValueEquationBuilder.setLength(0);
        unitNameBuilder.setLength(0);
        workingValueEquationBuilder.append(workingValueEquation);
        unitNameBuilder.append(unitName);
    }

    private String replaceByKeys(String expression, String value, String first) {
        if (nextToArithmetic(expression, first))
            expression = expression.replaceAll(first, value);

        return expression;
    }


    /**
     * another hack, I'm just checking if the value that I am about to replace is eligible to be replace.
     * with regular expression i can tell if it's alone, or next to some math character instead of numbers or letters
     *
     * @param expression
     * @param replacer
     * @return
     */
    private boolean nextToArithmetic(String expression, String replacer) {
        return Pattern.compile("(^%[*|\\/|+|-]|[*|\\/|+|-]%$|^%$|\\(%[*|\\/|+|-]|[*|\\/|+|-]%\\))"
                .replaceAll("%", replacer)).matcher(expression).find();
    }

    /**
     * users javasript engine to turn the string expression to the number it needs to be
     *
     * @param input
     * @return
     */
    private String StringEquationToResult(String input) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return engine.eval(String.format("Number(%s).toFixed(14)", input)).toString();
        } catch (ScriptException e) {
            log.info("incorrect input {}", input);
        }
        return "";
    }

    private List<Conversion> sortConversions(Comparator<Conversion> sort) {
        return conversionRepository.getAllConversions().sorted(sort).collect(Collectors.toList());
    }


}
