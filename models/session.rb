require_relative 'application_record'

class Session < ApplicationRecord
  belongs_to :course
  validates :date, presence: true
end
