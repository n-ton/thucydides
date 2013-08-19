package net.thucydides.core.reports.html

import com.google.common.collect.ImmutableList
import net.thucydides.core.issues.IssueTracking
import spock.lang.Specification
import spock.lang.Unroll

import java.security.acl.Owner

class WhenFormattingDataForTheHTMLReports extends Specification {

    IssueTracking issueTracking = Mock();

    @Unroll
    def "should display foreign characters as HTML entities"() {
        expect:
            def formatter = new Formatter(issueTracking);
            formatter.htmlCompatible(foreignWord) == formattedWord
        where:
            foreignWord         | formattedWord
            "François"          | "Fran&ccedil;ois"
            "störunterdrückung" | "st&ouml;runterdr&uuml;ckung"
            "CatÃ¡logo"         | "Cat&Atilde;&iexcl;logo"
    }

    @Unroll
    def "should display objects in string form"() {
        expect:
            def formatter = new net.thucydides.core.reports.html.Formatter(issueTracking);
            formatter.htmlCompatible(object) == formattedObject
        where:
            object                          | formattedObject
            [1,2,3]                         | "[1, 2, 3]"
            ["a":"1","b":2]                 | "{a=1, b=2}"
            ImmutableList.of("a","b","c")   | "[a, b, c]"
    }


    @Unroll
    def "should shorten long lines if requested"() {
        expect:
            def formatter = new net.thucydides.core.reports.html.Formatter(issueTracking);
            formatter.truncatedHtmlCompatible(value, length) == formattedValue
        where:
            value                 | length |  formattedValue
            "the quick brown dog" | 3      | "the&hellip;"
            "the quick brown dog" | 10     | "the quick&hellip;"
            "the quick brown dog" | 20     | "the quick brown dog"
            "François"            | 5      | "Fran&ccedil;&hellip;"
    }

    def "should format embedded tables"() {
        given:
            def stepDescription = """Given the following accounts:
   [| owner | points | statusPoints |
    | Jill  | 100000 | 800           |
    | Joe   | 50000  | 50            |]"""
        and:
            def formatter = new Formatter(issueTracking);
        when:
            def formattedDescription = formatter.formatWithFields(stepDescription, [])
        then:
            formattedDescription.contains("Given the following accounts:")
            formattedDescription.contains("<table class='embedded'><thead><th>owner</th><th>points</th><th>statusPoints</th></thead>")
    }

    def "should format single cell table"() {
        given:
            def singleCellTable = "[|heading|]"
            def formatter = new Formatter(issueTracking);
        when:
            def embeddedTable = formatter.convertAnyTables(singleCellTable)
        then:
            embeddedTable == "<table class='embedded'><thead><th>heading</th></thead><tbody></tbody></table>"
    }

    def "should format multi cell table"() {
        given:
            def singleCellTable = "[|heading1  |heading2  |]"
            def formatter = new Formatter(issueTracking);
        when:
            def embeddedTable = formatter.convertAnyTables(singleCellTable)
        then:
            embeddedTable == "<table class='embedded'><thead><th>heading1</th><th>heading2</th></thead><tbody></tbody></table>"
    }

    def "should format a table with a single row"() {
        given:
            def singleCellTable = """[| owner | points |
                                       | Joe   | 50000  |]"""

            def formatter = new Formatter(issueTracking);
        when:
            def embeddedTable = formatter.convertAnyTables(singleCellTable)
        then:
            embeddedTable == "<table class='embedded'><thead><th>owner</th><th>points</th></thead><tbody><tr><td>Joe</td><td>50000</td></tr></tbody></table>"
    }

    def "should format a table with several rows"() {
        given:
        def singleCellTable = """[| owner | points |
                                       | Jane  | 80000  |
                                       | Joe   | 50000  |]"""

        def formatter = new Formatter(issueTracking);
        when:
        def embeddedTable = formatter.convertAnyTables(singleCellTable)
        then:
        embeddedTable == "<table class='embedded'><thead><th>owner</th><th>points</th></thead><tbody><tr><td>Jane</td><td>80000</td></tr><tr><td>Joe</td><td>50000</td></tr></tbody></table>"
    }

}
