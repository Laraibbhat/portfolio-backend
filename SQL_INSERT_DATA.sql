-- ============================================
-- PORTFOLIO DATABASE - DATA INSERTION SCRIPT
-- For User: sadiyarashid1 (ID: 11)
-- ============================================

-- Verify user exists
SELECT * FROM users WHERE id = 11 AND username = 'sadiyarashid1';

-- ============================================
-- 1. EDUCATION DATA
-- ============================================
INSERT INTO education (user_id, degree, school, year, details, display_order) 
VALUES 
(11, 'B.Tech in Civil Engineering', 'National Institute of Technology (NIT) Srinagar', '2018', 'Specialized in Structural Engineering and Infrastructure Development', 1),
(11, 'M.A. in Public Relations', 'Kashmir University', '2022', 'Thesis on Healthcare Communication Strategies', 2),
(11, 'Diploma in Hospital Management', 'Institute of Healthcare Management', '2020', 'Focused on Operations and Patient Care Systems', 3);

-- Add education achievements
INSERT INTO education_achievements (education_id, achievement, display_order)
SELECT id, 'Dean List Student', 1 FROM education WHERE user_id = 11 AND degree = 'B.Tech in Civil Engineering'
UNION ALL
SELECT id, 'Gold Medalist in Infrastructure Studies', 2 FROM education WHERE user_id = 11 AND degree = 'B.Tech in Civil Engineering'
UNION ALL
SELECT id, 'Published Research on Healthcare Infrastructure', 1 FROM education WHERE user_id = 11 AND degree = 'M.A. in Public Relations';

-- ============================================
-- 2. TECHNICAL EXPERTISE DATA
-- ============================================
INSERT INTO technical_expertise (user_id, category, skill) 
VALUES 
(11, 'Healthcare IT', 'Hospital Management Systems (HMS)'),
(11, 'Healthcare IT', 'Electronic Health Records (EHR)'),
(11, 'Infrastructure', 'CAD and 3D Modeling'),
(11, 'Infrastructure', 'Building Information Modeling (BIM)'),
(11, 'Infrastructure', 'Facility Management'),
(11, 'Operations', 'Project Management'),
(11, 'Operations', 'Quality Assurance'),
(11, 'Operations', 'Staff Training and Development'),
(11, 'Communications', 'Stakeholder Management'),
(11, 'Communications', 'Public Relations');

-- ============================================
-- 3. EXPERIENCE DATA
-- ============================================
INSERT INTO experiences (user_id, title, company, period, project_focus, display_order) 
VALUES 
(11, 'Senior Hospital Administrator', 'Bone and Joint Hospital, Srinagar', '2023-Present', 'Leading hospital operations, managing 200+ staff, implementing new management systems', 1),
(11, 'Operations Manager', 'Bone and Joint Hospital, Srinagar', '2021-2023', 'Coordinated hospital infrastructure development, managed patient care workflows', 2),
(11, 'Infrastructure Coordinator', 'Bone and Joint Hospital, Srinagar', '2018-2021', 'Managed hospital expansion project, optimized facility operations', 3),
(11, 'Assistant Administrator', 'Regional Healthcare Center', '2016-2018', 'Supported administrative operations, managed scheduling and resources', 4);

-- Add experience highlights
INSERT INTO experience_highlights (experience_id, highlight, display_order)
SELECT id, 'Reduced operational costs by 25% through process optimization', 1 FROM experiences WHERE user_id = 11 AND title = 'Senior Hospital Administrator'
UNION ALL
SELECT id, 'Implemented new EHR system affecting 15 departments', 2 FROM experiences WHERE user_id = 11 AND title = 'Senior Hospital Administrator'
UNION ALL
SELECT id, 'Led team of 15 healthcare administrators', 3 FROM experiences WHERE user_id = 11 AND title = 'Senior Hospital Administrator'
UNION ALL
SELECT id, 'Supervised hospital expansion from 100 to 250 beds', 1 FROM experiences WHERE user_id = 11 AND title = 'Operations Manager'
UNION ALL
SELECT id, 'Improved patient satisfaction scores by 40%', 2 FROM experiences WHERE user_id = 11 AND title = 'Operations Manager'
UNION ALL
SELECT id, 'Managed ₹5 Crore capital improvement project', 1 FROM experiences WHERE user_id = 11 AND title = 'Infrastructure Coordinator';

-- ============================================
-- 4. CERTIFICATIONS DATA
-- ============================================
INSERT INTO certifications (user_id, name, issuer, certification_date, description, display_order) 
VALUES 
(11, 'Certified Healthcare Administrator (CHA)', 'National Association of Health Services Executives', '2023-06-15', 'Comprehensive certification in healthcare management and operations', 1),
(11, 'Six Sigma Green Belt', 'International Six Sigma Institute', '2022-09-20', 'Process improvement and quality management certification', 2),
(11, 'Project Management Professional (PMP)', 'Project Management Institute (PMI)', '2021-12-10', 'Professional project management certification', 3),
(11, 'Hospital Management Specialist', 'Institute of Healthcare Management', '2020-05-18', 'Specialized certification in hospital operations and management', 4),
(11, 'Advanced Leadership in Healthcare', 'Harvard Medical School', '2019-11-22', 'Executive leadership development program', 5);

-- ============================================
-- 5. AWARDS & RECOGNITION
-- ============================================
INSERT INTO awards (user_id, award_text, display_order) 
VALUES 
(11, 'Best Hospital Administrator - 2023 - Kashmir Healthcare Excellence Awards', 1),
(11, 'Employee of the Year - 2022 - Bone and Joint Hospital', 2),
(11, 'Outstanding Leadership Award - 2021 - Regional Healthcare Association', 3),
(11, 'Innovation in Healthcare Operations - 2020 - Health Ministry Award', 4),
(11, 'Excellence in Infrastructure Management - 2019 - Kashmir Development Authority', 5);

-- ============================================
-- 6. PUBLICATIONS
-- ============================================
INSERT INTO publications (user_id, title, journal, publication_date, description, display_order) 
VALUES 
(11, 'Optimizing Healthcare Delivery in Resource-Constrained Settings', 'Journal of Healthcare Management', '2023-03', 'A comprehensive study on improving hospital efficiency in Jammu & Kashmir', 1),
(11, 'Infrastructure Development for Modern Healthcare Facilities', 'International Journal of Health Systems', '2022-08', 'Best practices in hospital expansion and facility management', 2),
(11, 'Patient Care Workflows and Digital Transformation', 'Healthcare Technology Review', '2021-12', 'Implementation strategies for EHR systems in Indian hospitals', 3);

-- ============================================
-- 7. CORE COMPETENCIES
-- ============================================
INSERT INTO core_competencies (user_id, skill, level, display_order) 
VALUES 
(11, 'Hospital Operations Management', 'Expert', 1),
(11, 'Strategic Planning', 'Advanced', 2),
(11, 'Staff Leadership and Development', 'Advanced', 3),
(11, 'Infrastructure and Facility Management', 'Expert', 4),
(11, 'Healthcare IT Systems', 'Advanced', 5),
(11, 'Budget Planning and Financial Management', 'Advanced', 6),
(11, 'Patient Safety and Quality Assurance', 'Expert', 7),
(11, 'Process Improvement (Six Sigma)', 'Advanced', 8),
(11, 'Stakeholder Communication', 'Advanced', 9),
(11, 'Risk Management in Healthcare', 'Advanced', 10);

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Verify all data was inserted
SELECT 
  'Education' as entity_type, COUNT(*) as count FROM education WHERE user_id = 11
UNION ALL
SELECT 'Technical Expertise', COUNT(*) FROM technical_expertise WHERE user_id = 11
UNION ALL
SELECT 'Experiences', COUNT(*) FROM experiences WHERE user_id = 11
UNION ALL
SELECT 'Certifications', COUNT(*) FROM certifications WHERE user_id = 11
UNION ALL
SELECT 'Awards', COUNT(*) FROM awards WHERE user_id = 11
UNION ALL
SELECT 'Publications', COUNT(*) FROM publications WHERE user_id = 11
UNION ALL
SELECT 'Core Competencies', COUNT(*) FROM core_competencies WHERE user_id = 11;

-- Display all education with achievements
SELECT 
  e.id, e.degree, e.school, e.year,
  GROUP_CONCAT(ea.achievement, ', ') as achievements
FROM education e
LEFT JOIN education_achievements ea ON e.id = ea.education_id
WHERE e.user_id = 11
GROUP BY e.id;

-- Display all experiences with highlights
SELECT 
  ex.id, ex.title, ex.company, ex.period,
  GROUP_CONCAT(eh.highlight, ' | ') as highlights
FROM experiences ex
LEFT JOIN experience_highlights eh ON ex.id = eh.experience_id
WHERE ex.user_id = 11
GROUP BY ex.id;

-- Display all certifications
SELECT id, name, issuer, certification_date FROM certifications WHERE user_id = 11;

-- Display all awards
SELECT id, award_text FROM awards WHERE user_id = 11;

-- Display all publications
SELECT id, title, journal, publication_date FROM publications WHERE user_id = 11;

-- Display all core competencies
SELECT id, skill, level FROM core_competencies WHERE user_id = 11;

-- Display all technical expertise
SELECT category, GROUP_CONCAT(skill, ', ') as skills 
FROM technical_expertise 
WHERE user_id = 11 
GROUP BY category;
