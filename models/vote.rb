require_relative 'application_record'

class Vote < ApplicationRecord
 belongs_to :question
 end
