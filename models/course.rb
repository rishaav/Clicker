require_relative 'application_record'

class Course < ApplicationRecord
  validates :course_name, presence: true

end
