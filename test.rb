require 'rubygems'
require 'nokogiri'

def parseXML(filename)
  page = Nokogiri::XML(open(filename))
  puts page.class

page.css('//p').each do |a|

  puts "Question is " + a['qn'].to_s

  a.css('v').each do |e|

      puts e['fans']

      end
    end
  end

if __FILE__ == $0
  parseXML('CS 141 FALL 2016/SmallSession/L1609161320.xml')
end