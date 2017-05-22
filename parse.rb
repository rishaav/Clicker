# http://www.nokogiri.org/tutorials/searching_a_xml_html_document.html

require 'rubygems'
require 'nokogiri'
require_relative 'config/boot'
require_relative 'models/course'
require_relative 'models/question'
require_relative 'models/session'
require_relative 'models/vote'


def parseXML(filename)

  page = Nokogiri::XML(open(filename))
  puts page.class

  filname = filename.to_s
  puts "Filename is " + filname

  # puts "Sorted is " + sortcq

  cname = filname.match(/^([^\/]+)/)
  cname = cname.captures
  cname = cname.join(" ")
  #    puts "Class name is " + cname


  course = Course.find_or_create_by(course_name: cname)
  session = Session.find_or_create_by(course: course)
  sessdate = filname.match(/L(\d{6})/)
  sessdate = sessdate.captures
  sessdate = sessdate.join(" ")
  puts "Session Dates is " + sessdate

  sesstime = filname.match(/L\d{6}(.*).xml/)
  sesstime = sesstime.captures
  sesstime = sesstime.join(" ")



  page.css('//p').each do |a|
    session = Session.find_or_create_by(course: course, date: sessdate, time: sesstime)
    start =  a['strt']
    duration = start.match(/(\d+):(\d+):(\d+)/)
    hour,min,sec = duration.captures

    #  puts "Hour is: " + hour + " Min is " + min + " Sec is " + sec


    stop = a['stp']
    duration2 = stop.match(/(\d+):(\d+):(\d+)/)
    hour2,min2,sec2 = duration2.captures

    # puts "Hour2 is: " + hour2 + " Min2 is " + min2 + " Sec2 is " + sec2

    hour = hour.to_i
    min = min.to_i
    sec = sec.to_i

    hour2 = hour2.to_i
    min2 = min2.to_i
    sec2 = sec2.to_i

    fhour = hour2 - hour
    fmin = min2 - min
    fsec = sec2 - sec

    if fsec < 0
      fsec = -(fsec)
    end


    # puts "FHour is: " + fhour.to_s + " fmin is " + fmin.to_s + " fsec is " + fsec.to_s

    fhour = fhour.to_s
    fmin = fmin.to_s
    fsec = fsec.to_s


    if fhour.length == 1
      fhour = "0"+fhour
    end

    if fmin.length == 1
      fmin = "0"+fmin
    end

    if fsec.length == 1
      fsec = "0" + fsec
    end


    fduration = fhour + ":" + fmin + ":" + fsec

    #puts "Fduration is " + fduration


    # puts a['qn'] + " " + "Start Time: " + a['strt'] + " " + "Stop Time: " + a['stp']
    question = Question.find_by(name: a['qn'], session: session, answer: nil, start_time: a['strt'], duration: fduration, picture: nil, response: nil)
    if question == nil
      question = Question.find_or_create_by(name: a['qn'], session: session, answer: nil, start_time: a['strt'], duration: fduration, picture: nil, response: nil)
    end


    a.css('v').each do |e|

      #  puts e['id'] + " " + "Given answer: " + e['ans'] + " " + "First Answer Time " +  e['fanst'] + " " + e['fans'] + " " + e['tm']

      if e['id']!=nil && e['ans'] == nil
        e['ans'] = "N"
      end

      if e['id']!=nil && e['fans'] == nil
        e['fans'] = "N"
      end

      if e['id']!=nil && e['fanst'] == nil
        e['fanst'] = "XX:XX:XX"
      end

      if e['id']!=nil && e['tm'] == nil
        e['tm'] = "XX:XX:XX"
      end


      vote = Vote.find_by(clickerID: e['id'], question: question,  fanst: e['fanst'], fans: e['fans'], anst: e['tm'], ans: e['ans'])
      if vote == nil
        vote = Vote.find_or_create_by(clickerID: e['id'], question: question, fanst: e['fanst'], fans: e['fans'], anst: e['tm'], ans: e['ans'])

        end
      end
    end

end



def parseImg(picname)
  puts "Picture name is " + picname


  sortcq = picname.match(/_(\w)/)
  sortcq = sortcq.captures
  sortcq = sortcq.join(" ")

  dec = sortcq <=> 'Q'

  if dec == 0
    questionpic = picname
    puts "Questionpic is " + questionpic.to_s
  else
    answerpic = picname
    puts "Answerpic is " + answerpic.to_s
  end


  if questionpic != nil
    findsession = questionpic.match(/L(\d{3,6})/)
    findsession = findsession.captures
    findsession = findsession.join(" ")

    findtime = questionpic.match(/(\d{4})_/)
    findtime = findtime.captures
    findtime = findtime.join(" ")


    puts "Session is " + findsession


    sessionz = Session.find_by(date: findsession, time: findtime)

    if sessionz!=nil
      qnum = questionpic.match(/(\d+).jpg/)
      qnum = qnum.captures
      qnum = qnum.join(" ")
      qnum = "Question " + qnum
      questionz = Question.find_by(name: qnum, session: sessionz)
      if questionz != nil
        pictures = File.open(questionpic, "rb"){|io|io.read}

        questionz.update(picture: pictures)

      end


    end
  end


  if answerpic != nil
    findsession = answerpic.match(/L(\d{3,6})/)
    findsession = findsession.captures
    findsession = findsession.join(" ")

    findtime = answerpic.match(/(\d{4})_/)
    findtime = findtime.captures
    findtime = findtime.join(" ")



    puts "Session is " + findsession


    sessionz = Session.find_by(date: findsession, time: findtime)

    if sessionz!=nil
      qnum = answerpic.match(/(\d+).jpg/)
      qnum = qnum.captures
      qnum = qnum.join(" ")
      qnum = "Question " + qnum
      questionz = Question.find_by(name: qnum, session: sessionz)
      if questionz != nil
        pictures = File.open(answerpic, "rb"){|io|io.read}

        questionz.update(response: pictures)

      end


    end
  end

end





def main
  root = 'CS 141 FALL 2016'

  Dir[root+'/SessionData/*'].each do |session|
    # puts session
    parseXML(session)
  end

  Dir[root+'/Images/*'].each do |imags|
#      puts imgs
    parseImg(imags)
 end



end


if __FILE__ == $0
  #parseXML('CS 141 FALL 2016/SmallSession/L1609161320.xml')
 main
end


