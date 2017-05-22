require_relative 'application_record'

class Question < ApplicationRecord
 belongs_to :session
 validates :name, presence: true
  validates :start_time, presence: true
  validates :duration, presence: true
end
